package com.tk.gg.users.domain.model;

import com.tk.gg.common.enums.Gender;
import com.tk.gg.common.enums.UserRole;
import com.tk.gg.common.jpa.BaseEntity;
import com.tk.gg.users.application.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Table(name = "p_users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false)
    private String username;

    @Setter
    @Column(name = "password", nullable = false)
    private String password;

    @Setter
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Setter
    @Column(name = "phone_number")
    private String phoneNumber;

    @Setter
    @Column(name = "address")
    private String address;

    @Setter
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public static User of(UserDto dto){
        return User.builder()
                .userId(dto.userId())
                .email(dto.email())
                .username(dto.username())
                .password(dto.password())
                .role(dto.role())
                .gender(dto.gender())
                .phoneNumber(dto.phoneNumber())
                .address(dto.address())
                .isDeleted(dto.isDeleted())
                .build();
    }

    public static User test() {
        return User.builder()
                .userId(1L)
                .email("test")
                .username("test")
                .password("test")
                .build();
    }

    public void delete() {
        this.isDeleted = true;
    }
}
