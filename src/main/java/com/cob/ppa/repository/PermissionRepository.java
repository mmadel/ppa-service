package com.cob.ppa.repository;

import com.cob.ppa.entity.security.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    // Basic CRUD operations are inherited from JpaRepository

    // Find permission by name
    Optional<Permission> findByName(String name);

    // Check if permission exists by name
    boolean existsByName(String name);

    // Find all permissions assigned to a specific role
    @Query("SELECT p FROM Permission p JOIN p.roles r WHERE r.id = :roleId")
    Set<Permission> findPermissionsByRoleId(Long roleId);

    // Find all permissions assigned to a specific user
    @Query("SELECT DISTINCT p FROM Permission p JOIN p.roles r JOIN r.users u WHERE u.id = :userId")
    Set<Permission> findPermissionsByUserId(Long userId);

    // Check if a role has a specific permission
    @Query("SELECT COUNT(p) > 0 FROM Permission p JOIN p.roles r WHERE r.id = :roleId AND p.name = :permissionName")
    boolean roleHasPermission(Long roleId, String permissionName);

    // Find permissions not assigned to a role
    @Query("SELECT p FROM Permission p WHERE p.id NOT IN " +
            "(SELECT p2.id FROM Permission p2 JOIN p2.roles r WHERE r.id = :roleId)")
    List<Permission> findAvailablePermissionsForRole(Long roleId);

    // Find all permissions with pagination
    @Query("SELECT p FROM Permission p ORDER BY p.name")
    List<Permission> findAllPermissions();

    // Search permissions by name pattern
    @Query("SELECT p FROM Permission p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<Permission> searchPermissions(String keyword);
}