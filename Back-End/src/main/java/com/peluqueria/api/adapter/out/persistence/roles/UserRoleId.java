package com.peluqueria.api.adapter.out.persistence.roles;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * The Class UserRoleId.
 */
// Debe implementar Serializable
@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleId implements Serializable {

    /** The user id. */
    private Long userId; // Debe coincidir con el nombre del campo en UserRolesEntity
    
    /** The role id. */
    private Long roleId; // Debe coincidir con el nombre del campo en UserRolesEntity

    /**
     * Equals.
     *
     * @param o the o
     * @return true, if successful
     */
    // Implementación de equals y hashCode es OBLIGATORIA para claves compuestas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleId that = (UserRoleId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(roleId, that.roleId);
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }
}