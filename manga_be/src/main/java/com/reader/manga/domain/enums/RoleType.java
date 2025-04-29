package com.reader.manga.domain.enums;

public enum RoleType {

    USER("Usuário"),
    ADMIN("Administrador"),
    MODERATOR("Moderador")
    ;

    private final String roleDescription;

    RoleType(String roleDescription) {
      this.roleDescription = roleDescription;
    }

}
