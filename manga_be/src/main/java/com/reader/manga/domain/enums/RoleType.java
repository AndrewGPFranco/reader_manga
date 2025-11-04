package com.reader.manga.domain.enums;

import java.util.Set;

public enum RoleType {

    USER("Usuário", 1L),
    MODERATOR("Moderador", 2L),
    ADMIN("Administrador", 3L),
    OWNER("Proprietário", 4L),
    ;

    private final Long pesoRole;
    private final String roleDescription;

    RoleType(String roleDescription, Long pesoRole) {
        this.pesoRole = pesoRole;
        this.roleDescription = roleDescription;
    }

    public static RoleType getRolePrincipal(Set<RoleType> roles) {
        RoleType role = RoleType.USER;

        for (RoleType r : roles) {
            if (r.pesoRole > role.pesoRole)
                role = r;
        }

        return role;
    }

}
