package com.tk.gg.users.infra.repository.profile;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
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

import java.util.List;
import java.util.Optional;

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

        List<ProfilePageResponse> profilePageResponses = queryFactory
                .select(Projections.constructor(
                        ProfilePageResponse.class,
                        profile.profileId,
                        user.userId,
                        user.username,
                        profile.profileImageUrl,
                        // Style list mapping
                        Projections.list(Projections.constructor(
                                StyleResponse.class,
                                preferStyle.preferStyleId,
                                preferStyle.styleName
                        )),
                        // Price list mapping
                        Projections.list(Projections.constructor(
                                PriceResponse.class,
                                preferPrice.preferPriceId,
                                preferPrice.price
                        )),
                        // Location list mapping
                        Projections.list(Projections.constructor(
                                LocationResponse.class,
                                preferLocation.preferLocationId,
                                preferLocation.locationName
                        )),
                        profile.createdAt
                ))
                .from(profile)
                .join(profile.user, user)
                .leftJoin(profile.preferStyles, preferStyle)
                .leftJoin(profile.preferPrices, preferPrice)
                .leftJoin(profile.preferLocations, preferLocation)
                .where(conditions.and(profile.isDeleted.eq(false)))
                .orderBy(profile.createdAt.desc())
                .offset(pageable.getOffset())   // Pagination offset
                .limit(pageable.getPageSize())  // Page size
                .fetch();  // fetch the paginated results

        // Fetch total count for pagination
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


}
