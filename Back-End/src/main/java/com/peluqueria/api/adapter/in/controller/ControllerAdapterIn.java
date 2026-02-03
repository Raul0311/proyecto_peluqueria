package com.peluqueria.api.adapter.in.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.peluqueria.api.adapter.out.persistence.addresses.AddressEntity.AddressType;
import com.peluqueria.api.application.ports.in.*;
import com.peluqueria.api.application.rolecases.RoleUpdateCommand;
import com.peluqueria.api.domain.*;
import com.peluqueria.api.domain.dto.*;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * The Class ControllerAdapterIn adaptado para WebFlux.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class ControllerAdapterIn {
	
	/** The Constant ADMIN. */
	private static final String ADMIN = "ROLE_ADMIN";
	
	/** The address port in. */
	private final AddressPortIn addressPortIn;
	
	/** The user port in. */
	private final UserPortIn userPortIn;
	
	/** The roles port in. */
	private final RolesPortIn rolesPortIn;
	
	/** The appointment port in. */
	private final AppointmentPortIn appointmentPortIn;
	
	/** The closed day port in. */
	private final ClosedDayPortIn closedDayPortIn;
	
	/** The day exceptionport in. */
	private final DayExceptionPortIn dayExceptionportIn;
	
	/** The haircut service port in. */
	private final HaircutServicePortIn haircutServicePortIn;

    /**
     * Helper para ejecutar tareas bloqueantes en un hilo elástico.
     *
     * @param <T> the generic type
     * @param callable the callable
     * @return the mono
     */
	private <T> Mono<T> async(java.util.concurrent.Callable<T> callable) {
	    return Mono.fromCallable(callable)
	            .subscribeOn(Schedulers.boundedElastic());
	}

    /**
     * Helper para ejecutar tareas bloqueantes sin retorno.
     *
     * @param runnable the runnable
     * @return the mono
     */
	private Mono<Void> asyncVoid(Runnable runnable) {
        return Mono.<Void>fromRunnable(runnable)
                .subscribeOn(Schedulers.boundedElastic());
    }

	/**
	 * Gets the addresses.
	 *
	 * @return the addresses
	 */
	@GetMapping("/getAddresses")
    @Operation(summary = "Obtener direcciones", description = "Devuelve las direcciones de facturación y de envío del usuario")
    public Flux<Address> getAddresses() {
        return ReactiveSecurityContextHolder.getContext()
            .map(ctx -> (Long) ctx.getAuthentication().getPrincipal())
            .flatMapMany(userId -> async(() -> addressPortIn.load(userId)).flatMapMany(Flux::fromIterable));
    }
	
	/**
	 * Creates the address.
	 *
	 * @param address the address
	 * @return the mono
	 */
	@PostMapping("/setAddress")
	@Operation(summary = "Crear una dirección", description = "Crea una dirección ya sea de facturación o de envío del usuario")
    public Mono<Address> createAddress(@RequestBody Address address) {
        return ReactiveSecurityContextHolder.getContext()
            .map(ctx -> (Long) ctx.getAuthentication().getPrincipal())
            .flatMap(userId -> async(() -> {
                address.setUserId(userId);
                return addressPortIn.save(address);
            }));
    }

    /**
     * Update address.
     *
     * @param address the address
     * @return the mono
     */
    @PutMapping("/updateAddress")
    @Operation(summary = "Modificar una dirección", description = "Modifica una dirección ya sea de facturación o de envío del usuario")
    public Mono<Address> updateAddress(@RequestBody Address address) {
        return async(() -> addressPortIn.update(address));
    }

    /**
     * Delete address.
     *
     * @param id the id
     * @return the mono
     */
    @DeleteMapping("/deleteAddress/{id}")
    @Operation(summary = "Eliminar una dirección", description = "Elimina una dirección ya sea de facturación o de envío del usuario")
    public Mono<ResponseEntity<Void>> deleteAddress(@PathVariable Long id) {
        return ReactiveSecurityContextHolder.getContext()
            .map(ctx -> (Long) ctx.getAuthentication().getPrincipal())
            .flatMap(userId -> asyncVoid(() -> addressPortIn.delete(userId, id))
                .thenReturn(ResponseEntity.ok().<Void>build()));
    }
    
    /**
     * Sets the default address.
     *
     * @param id the id
     * @param type the type
     * @return the mono
     */
    @PutMapping("/{id}/default")
    @Operation(summary = "Cambiar dirección predeterminada", description = "Cambia la dirección predeterminada ya sea de facturación o de envío del usuario")
    public Mono<ResponseEntity<Void>> setDefaultAddress(@PathVariable Long id, @RequestParam AddressType type) {
        return ReactiveSecurityContextHolder.getContext()
            .map(ctx -> (Long) ctx.getAuthentication().getPrincipal())
            .flatMap(userId -> asyncVoid(() -> addressPortIn.setDefault(userId, id, type))
                .thenReturn(ResponseEntity.ok().<Void>build()));
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    @GetMapping("/getUser")
    public Mono<User> getUser() {
        return ReactiveSecurityContextHolder.getContext()
            .map(ctx -> (Long) ctx.getAuthentication().getPrincipal())
            .flatMap(userId -> async(() -> userPortIn.load(userId)));
    }
    
    /**
     * List users for booking.
     *
     * @return the mono
     */
    @GetMapping("/users-list")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtiene los usuarios", description = "Obtiene todos los usuarios del sistema")
    public Mono<ResponseEntity<List<UserSimpleDto>>> listUsersForBooking() {
        return async(() -> ResponseEntity.ok(userPortIn.findAllForSelection()));
    }
    
    /**
     * Update user.
     *
     * @param user the user
     * @return the mono
     */
    @PutMapping("/updateUser")
    @Operation(summary = "Modificar el usuario", description = "Modifica el usuario por id")
    public Mono<User> updateUser(@RequestBody User user) {
        return async(() -> userPortIn.update(user));
    }
    
    /**
     * Disable user.
     *
     * @return the mono
     */
    @PutMapping("/users/disable")
    @Operation(summary = "Eliminar cuenta del usuario", description = "Elimina la cuenta del usuario poniendo el campo enabled a false")
    public Mono<ResponseEntity<Void>> disableUser() {
        return ReactiveSecurityContextHolder.getContext()
            .map(ctx -> (Long) ctx.getAuthentication().getPrincipal())
            .flatMap(userId -> asyncVoid(() -> userPortIn.disableUser(userId))
                .thenReturn(ResponseEntity.ok().<Void>build()));
    }
    
    /**
     * List all users with roles.
     *
     * @return the mono
     */
    @GetMapping("/admin/usersWithRoles")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener usuarios con roles", description = "Devuelve todos los usuarios con los roles de cada uno")
    public Mono<ResponseEntity<List<UserWithRolesDto>>> listAllUsersWithRoles() {
        return async(() -> ResponseEntity.ok(rolesPortIn.getAllUsersWithRoles()));
    }
    
    /**
     * List all roles.
     *
     * @return the mono
     */
    @GetMapping("/admin/roles")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener roles", description = "Devuelve todos los roles que tiene la aplicación")
    public Mono<ResponseEntity<List<RoleDto>>> listAllRoles() {
        return async(() -> ResponseEntity.ok(rolesPortIn.getAllRoles()));
    }
    
    /**
     * Update user roles.
     *
     * @param command the command
     * @return the mono
     */
    @PutMapping("/admin/updateUserRoles")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar roles", description = "Actualiza los roles que se han cambiado")
    public Mono<ResponseEntity<Void>> updateUserRoles(@RequestBody RoleUpdateCommand command) {
        return ReactiveSecurityContextHolder.getContext()
            .map(ctx -> (Long) ctx.getAuthentication().getPrincipal())
            .flatMap(authenticatedUserId -> asyncVoid(() -> rolesPortIn.updateUserRoles(authenticatedUserId, command))
                .thenReturn(ResponseEntity.noContent().<Void>build()));
    }
    
    /**
     * Creates the new role.
     *
     * @param roleDto the role dto
     * @return the mono
     */
    @PostMapping("/admin/roles") 
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear un nuevo rol", description = "Crea un nuevo rol con nombre y descripción.")
    public Mono<ResponseEntity<Void>> createNewRole(@RequestBody RoleCreationDto roleDto) {
        return asyncVoid(() -> rolesPortIn.createNewRole(roleDto))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());
    }
    
    /**
     * Delete role.
     *
     * @param roleName the role name
     * @return the mono
     */
    @DeleteMapping("/admin/roles/{roleName}")
    @PreAuthorize("hasRole('ADMIN')") 
    @Operation(summary = "Eliminar un rol", description = "Elimina un rol existente que no sea ROLE_ADMIN o ROLE_USER.")
    public Mono<ResponseEntity<Void>> deleteRole(@PathVariable String roleName) {
        return asyncVoid(() -> rolesPortIn.deleteRole(roleName))
                .thenReturn(ResponseEntity.noContent().<Void>build());
    }
    
    /**
     * List appointments.
     *
     * @param type the type
     * @return the flux
     */
    @GetMapping("/getAppointment/{type}")
    @Operation(summary = "Obtener citas", description = "Obtiene todas las citas si eres admin o solo las citas del usuario")
    public Flux<Appointment> list(@PathVariable String type) {
        return ReactiveSecurityContextHolder.getContext()
            .map(ctx -> ctx.getAuthentication())
            .map(Optional::of) // Envolvemos en Optional para evitar flujos vacíos
            .defaultIfEmpty(Optional.empty()) 
            .flatMapMany(authOpt -> {
                // Extraemos los datos de forma segura
                Long userId = authOpt
                    .filter(auth -> auth.isAuthenticated() && auth.getPrincipal() instanceof Long)
                    .map(auth -> (Long) auth.getPrincipal())
                    .orElse(null);

                boolean isAdmin = authOpt
                    .map(auth -> auth.getAuthorities().stream()
                                     .anyMatch(a -> a.getAuthority().equals(ADMIN)))
                    .orElse(false);

                // Ejecutamos la lógica bloqueante en el scheduler elástico
                return async(() -> appointmentPortIn.getAppointments(userId, isAdmin))
                        .flatMapMany(Flux::fromIterable);
            });
    }

    /**
     * Creates the reserve.
     *
     * @param appointment the appointment
     * @return the mono
     */
    @PostMapping("/create")
    public Mono<Appointment> reserve(@RequestBody Appointment appointment) {
        return ReactiveSecurityContextHolder.getContext()
            .map(ctx -> ctx.getAuthentication())
            .flatMap(auth -> {
                final Long authenticatedUserId = (Long) auth.getPrincipal();
                final boolean isAdmin = auth.getAuthorities().stream()
                                      .anyMatch(a -> a.getAuthority().equals(ADMIN));

                return async(() -> {
                    if (!isAdmin) {
                        appointment.setUserId(authenticatedUserId);
                    }
                    return appointmentPortIn.create(appointment);
                });
            });
    }
    
    /**
     * Update appointment status.
     *
     * @param id the id
     * @param status the status
     * @return the mono
     */
    @PutMapping("/appointments/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar estado de cita", description = "Permite al admin confirmar o cancelar una cita")
    public Mono<ResponseEntity<Void>> updateAppointmentStatus(@PathVariable Long id, @RequestParam String status) {
        return ReactiveSecurityContextHolder.getContext()
            .map(ctx -> ctx.getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(ADMIN)))
            .flatMap(isAdmin -> asyncVoid(() -> appointmentPortIn.updateStatus(id, status, isAdmin))
                .thenReturn(ResponseEntity.ok().<Void>build()));
    }
    
    /**
     * Gets the closed days.
     *
     * @return the closed days
     */
    @GetMapping("/closed-days")
    public Mono<ResponseEntity<List<ClosedDay>>> getClosedDays() {
        return async(() -> ResponseEntity.ok(closedDayPortIn.findAll()));
    }

    /**
     * Toggle day.
     *
     * @param dateStr the date str
     * @return the mono
     */
    @PostMapping("/closed-days/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<Void>> toggleDay(@RequestParam("date") String dateStr) {
        return asyncVoid(() -> closedDayPortIn.toggleDay(LocalDate.parse(dateStr)))
                .thenReturn(ResponseEntity.ok().build());
    }
    
    /**
     * Gets the all.
     *
     * @return the all
     */
    @GetMapping("/get-day-exceptions")
    public Flux<DayExceptionDto> getAll() {
        return async(() -> dayExceptionportIn.findAllExceptions()).flatMapMany(Flux::fromIterable);
    }

    /**
     * Save.
     *
     * @param dto the dto
     * @return the mono
     */
    @PostMapping("/save-day-exceptions")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<Void>> save(@RequestBody DayExceptionDto dto) {
    	return asyncVoid(() -> dayExceptionportIn.saveException(dto))
                .thenReturn(ResponseEntity.ok().build());
    }
    
    /**
     * List haircut service.
     *
     * @return the flux
     */
    @GetMapping("/get-haircut-services")
    public Flux<HaircutService> listHaircutService() {
    	return async(() -> haircutServicePortIn.getAllAvailable()).flatMapMany(Flux::fromIterable);
	}

    /**
     * Creates the haircut service.
     *
     * @param s the s
     * @return the mono
     */
    @PostMapping("/create-haircut-services")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<HaircutService> createHaircutService(@RequestBody HaircutService s) { 
    	return async(() -> haircutServicePortIn.save(s));
	}

    /**
     * Update haircut service.
     *
     * @param id the id
     * @param s the s
     * @return the mono
     */
    @PutMapping("/update-haircut-services/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<HaircutService> updateHaircutService(@PathVariable Long id, @RequestBody HaircutService s) { 
    	return async(() -> haircutServicePortIn.update(id, s));
	}

    /**
     * Delete haircut service.
     *
     * @param id the id
     * @return the mono
     */
    @DeleteMapping("/delete-haircut-services/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Void> deleteHaircutService(@PathVariable Long id) { 
        return asyncVoid(() -> haircutServicePortIn.delete(id));
    }
    
    /**
     * Gets the occupied slots.
     *
     * @param date the date
     * @return the occupied slots
     */
    @GetMapping("/occupied-slots/{date}")
    public Flux<TimeRangeDto> getOccupiedSlots(@PathVariable LocalDate date) {
    	return async(() -> appointmentPortIn.getOccupiedSlots(date)).flatMapMany(Flux::fromIterable);
    }
}