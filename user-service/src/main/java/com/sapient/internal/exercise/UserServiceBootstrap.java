package com.sapient.internal.exercise;

import com.sapient.internal.exercise.entities.Permission;
import com.sapient.internal.exercise.entities.Role;
import com.sapient.internal.exercise.entities.User;
import com.sapient.internal.exercise.service.AdminService;
import com.sapient.internal.exercise.service.PermissionService;
import com.sapient.internal.exercise.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class UserServiceBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceBootstrap.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private AdminService adminService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.info("event source: {}", event.getSource());
        Set<Permission> userPermissions = saveUserPermissions();
        saveRole("User", "Role for users", userPermissions);

        Set<Permission> adminPermissions = saveAdminPermissions();
        adminPermissions.addAll(userPermissions);
        saveRole("Admin", "Role for admins", adminPermissions);
        createSuperAdmin();
    }

    void createSuperAdmin() {
        User user = new User("Admin", null, "admin@hotmail.com", "admin", 9897966059L);
        try {
            user = adminService.createAdmin(user);
            LOGGER.info("Admin created with email: {}", user.getEmail());
        } catch (Exception e) {
            LOGGER.info("Failed to create admin! Exiting the app!");
        }

    }

    private Set<Permission> saveUserPermissions() {
        Set<Permission> userPermissions = new LinkedHashSet<>();
        userPermissions.add(permissionService.savePermission(new Permission("get_me", "To fetch the details of self")));
        userPermissions.add(permissionService.savePermission(new Permission("update_profile", "To update the details of self")));
        userPermissions.add(permissionService.savePermission(new Permission("SWIPE_CARD", "To save the swipe card time")));
        userPermissions.add(permissionService.savePermission(new Permission("FIND_SELF_ATTENDANCE", "To fetch the attendance record of self")));
        return userPermissions;
    }

    private Set<Permission> saveAdminPermissions() {
        Set<Permission> adminPermissions = new LinkedHashSet<>();
        adminPermissions.add(permissionService.savePermission(new Permission("find_all", "To fetch the details of all user")));
        adminPermissions.add(permissionService.savePermission(new Permission("update_user_profile", "To update the details of any user")));
        adminPermissions.add(permissionService.savePermission(new Permission("UPDATE_SWIPE", "To update the swipe details manually")));
        adminPermissions.add(permissionService.savePermission(new Permission("FIND_ATTENDANCE", "To fetch the details of attendance records")));
        adminPermissions.add(permissionService.savePermission(new Permission("FIND_USER", "To fetch the user details")));
        return adminPermissions;
    }

    private void saveRole(String roleName, String description, Set<Permission> permissions) {
        Role role = new Role(roleName, description, permissions);
        role = roleService.saveRole(role);
        LOGGER.info("Role saved in DB with id: {} and name: {}", role.getId(), role.getRoleName());
    }
}
