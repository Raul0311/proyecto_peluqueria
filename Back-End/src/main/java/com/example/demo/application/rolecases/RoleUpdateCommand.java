package com.example.demo.application.rolecases;

import java.util.List;

/**
 * The Record RoleUpdateCommand.
 *
 * @param userId the user id
 * @param roleNames the role names
 */
public record RoleUpdateCommand(Long userId, List<String> roleNames) {}