package com.cob.ppa.repository;

import com.cob.ppa.entity.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Basic CRUD operations are inherited from JpaRepository

    // Find role by name
    Optional<Role> findByName(String name);

    // Check if role exists by name
    boolean existsByName(String name);

    // Find all roles assigned to a specific user
    @Query("SELECT r FROM Role r JOIN r.users u WHERE u.id = :userId")
    Set<Role> findRolesByUserId(Long userId);

    // Find all roles that have a specific permission
    @Query("SELECT DISTINCT r FROM Role r JOIN r.permissions p WHERE p.name = :permissionName")
    List<Role> findRolesByPermissionName(String permissionName);

    // Check if a specific user has a role
    @Query("SELECT COUNT(r) > 0 FROM Role r JOIN r.users u WHERE u.id = :userId AND r.name = :roleName")
    boolean userHasRole(Long userId, String roleName);

    // Add a permission to a role
    @Modifying
    @Query(value = "INSERT INTO role_permissions (role_id, permission_id) VALUES (:roleId, :permissionId)",
            nativeQuery = true)
    void addPermissionToRole(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    // Remove a permission from a role
    @Modifying
    @Query(value = "DELETE FROM role_permissions WHERE role_id = :roleId AND permission_id = :permissionId",
            nativeQuery = true)
    void removePermissionFromRole(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    // Count users with a specific role
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = :roleName")
    long countUsersByRoleName(String roleName);

    // Find all roles with pagination
    @Query("SELECT r FROM Role r ORDER BY r.name")
    List<Role> findAllRoles();
}