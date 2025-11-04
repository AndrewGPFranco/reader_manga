package com.reader.manga.domain.entities.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reader.manga.domain.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "users")
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<RoleType> roles;

    @Column(nullable = false)
    private LocalDate dateBirth;

    private String uriProfilePhoto;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDate createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserManga> userMangas;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteMangaUser> mangaFavorites;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteEpisodeUser> favoriteEpisodeUsers;

    public User(String username, String fullName, String firstName, LocalDate dateBirth, Set<RoleType> roles, String password, String email) {
        this.username = username;
        this.fullName = fullName;
        this.firstName = firstName;
        this.dateBirth = dateBirth;
        this.roles = roles;
        this.password = password;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public RoleType getRolePrincipal() {
        Set<RoleType> roles = getRoles();
        return RoleType.getRolePrincipal(roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", dateBirth=" + dateBirth +
                ", uriProfilePhoto='" + uriProfilePhoto + '\'' +
                ", createdAt=" + createdAt +
                ", userMangas=" + userMangas +
                ", mangaFavorites=" + mangaFavorites +
                ", favoriteEpisodeUsers=" + favoriteEpisodeUsers +
                '}';
    }
}
