package com.tk.gg.users.infra.repository.profile;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tk.gg.users.domain.model.*;
import com.tk.gg.users.presenation.request.ProfileSearch;
import com.tk.gg.users.presenation.response.LocationResponse;
import com.tk.gg.users.presenation.response.PriceResponse;
import com.tk.gg.users.presenation.response.ProfilePageResponse;
import com.tk.gg.users.presenation.response.StyleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static com.tk.gg.users.domain.model.QPreferLocation.preferLocation;
import static com.tk.gg.users.domain.model.QPreferPrice.preferPrice;
import static com.tk.gg.users.domain.model.QPreferStyle.preferStyle;
import static com.tk.gg.users.domain.model.QUser.user;

@RequiredArgsConstructor
public class ProfileQueryDslRepositoryImpl implements ProfileQueryDslRepository {
    private final JPAQueryFactory queryFactory;


    @Override
    public Page<ProfilePageResponse> searchProfiles(ProfileSearch profileSearch, Pageable pageable) {
        QProfile profile = QProfile.profile;
        QUser user = QUser.user;
        QPreferStyle preferStyle = QPreferStyle.preferStyle;
        QPreferPrice preferPrice = QPreferPrice.preferPrice;
        QPreferLocation preferLocation = QPreferLocation.preferLocation;

        BooleanBuilder conditions = getBooleanBuilder(profileSearch);

        // 먼저 모든 데이터를 튜플 형태로 가져옵니다.
        List<Tuple> resultTuples = queryFactory
                .select(
                        profile.profileId,
                        user.userId,
                        user.username,
                        profile.profileImageUrl,
                        preferStyle.preferStyleId,
                        preferStyle.styleName,
                        preferPrice.preferPriceId,
                        preferPrice.price,
                        preferLocation.preferLocationId,
                        preferLocation.locationName,
                        profile.createdAt
                )
                .from(profile)
                .join(profile.user, user)
                .leftJoin(profile.preferStyles, preferStyle)
                .leftJoin(profile.preferPrices, preferPrice)
                .leftJoin(profile.preferLocations, preferLocation)
                .where(conditions.and(profile.isDeleted.eq(false)))
                .orderBy(profile.createdAt.desc())
                .offset(pageable.getOffset())   // Pagination offset
                .limit(pageable.getPageSize())  // Page size
                .fetch();

        // 중복 제거 및 ProfilePageResponse 생성
        Map<UUID, ProfilePageResponse> profileResponseMap = new LinkedHashMap<>();

        for (Tuple tuple : resultTuples) {
            UUID profileId = tuple.get(profile.profileId);

            ProfilePageResponse profilePageResponse = profileResponseMap.computeIfAbsent(profileId, id ->
                    new ProfilePageResponse(
                            id,
                            tuple.get(user.userId),
                            tuple.get(user.username),
                            tuple.get(profile.profileImageUrl),
                            new ArrayList<>(), // 스타일 리스트
                            new ArrayList<>(), // 가격 리스트
                            new ArrayList<>(), // 위치 리스트
                            tuple.get(profile.createdAt)
                    )
            );

            // 스타일, 가격, 위치 리스트 추가
            addToProfileList(profilePageResponse, tuple, preferStyle, preferPrice, preferLocation);
        }

        List<ProfilePageResponse> profilePageResponses = new ArrayList<>(profileResponseMap.values());

        // 총 카운트를 가져옵니다.
        Long total = Optional.ofNullable(
                        queryFactory
                                .select(profile.count())
                                .from(profile)
                                .where(profile.isDeleted.eq(false))
                                .fetchOne()
                )
                .orElse(0L);

        return new PageImpl<>(profilePageResponses, pageable, total);
    }


    private static BooleanBuilder getBooleanBuilder(ProfileSearch profileSearch) {
        BooleanBuilder conditions = new BooleanBuilder();

        if (profileSearch.role() != null) {
            conditions.and(user.role.eq(profileSearch.role()));
        }

        addConditionsForList(conditions, profileSearch.styleList(), preferStyle.styleName, preferStyle.isDeleted);
        addConditionsForLongList(conditions, profileSearch.priceList(), preferPrice.price, preferPrice.isDeleted);
        addConditionsForList(conditions, profileSearch.locationList(), preferLocation.locationName, preferLocation.isDeleted);

        return conditions;
    }

    private static <T> void addConditionsForList(BooleanBuilder conditions, List<String> list,
                                                 StringPath fieldName, BooleanExpression isDeleted) {
        if (list != null && !list.isEmpty()) {
            conditions.and(fieldName.in(list)).and(isDeleted.eq(false));
        } else {
            conditions.and(isDeleted.eq(false));
        }
    }

    private static void addConditionsForLongList(BooleanBuilder conditions, List<Long> list,
                                                 NumberPath<Long> fieldName, BooleanExpression isDeleted) {
        if (list != null && !list.isEmpty()) {
            conditions.and(fieldName.in(list)).and(isDeleted.eq(false));
        } else {
            conditions.and(isDeleted.eq(false));
        }
    }

    private void addToProfileList(ProfilePageResponse profilePageResponse, Tuple tuple, QPreferStyle preferStyle, QPreferPrice preferPrice, QPreferLocation preferLocation) {
        // 스타일 리스트 추가 (중복 체크)
        if (tuple.get(preferStyle.preferStyleId) != null) {
            StyleResponse newStyle = new StyleResponse(
                    tuple.get(preferStyle.preferStyleId),
                    tuple.get(preferStyle.styleName)
            );
            if (profilePageResponse.styleList().stream().noneMatch(s -> s.preferStyleId().equals(newStyle.preferStyleId()))) {
                profilePageResponse.styleList().add(newStyle);
            }
        }

        // 가격 리스트 추가 (중복 체크)
        if (tuple.get(preferPrice.preferPriceId) != null) {
            PriceResponse newPrice = new PriceResponse(
                    tuple.get(preferPrice.preferPriceId),
                    tuple.get(preferPrice.price)
            );
            if (profilePageResponse.priceList().stream().noneMatch(p -> p.preferPriceId().equals(newPrice.preferPriceId()))) {
                profilePageResponse.priceList().add(newPrice);
            }
        }

        // 위치 리스트 추가 (중복 체크)
        if (tuple.get(preferLocation.preferLocationId) != null) {
            LocationResponse newLocation = new LocationResponse(
                    tuple.get(preferLocation.preferLocationId),
                    tuple.get(preferLocation.locationName)
            );
            if (profilePageResponse.locationList().stream().noneMatch(l -> l.preferLocationId().equals(newLocation.preferLocationId()))) {
                profilePageResponse.locationList().add(newLocation);
            }
        }
    }


}
