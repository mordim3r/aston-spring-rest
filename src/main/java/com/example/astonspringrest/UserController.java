package com.example.astonspringrest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Операции с пользователями")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Создать пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<UserDto> create(@RequestBody UserDto dto) {
        UserDto created = userService.create(dto);
        return toModel(created);
    }

    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public EntityModel<UserDto> getById(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable int id
    ) {
        return toModel(userService.getById(id));
    }

    @Operation(summary = "Получить всех пользователей")
    @GetMapping
    public CollectionModel<EntityModel<UserDto>> getAll() {
        List<EntityModel<UserDto>> users = userService.getAll()
                .stream()
                .map(this::toModel)
                .toList();

        return CollectionModel.of(
                users,
                linkTo(methodOn(UserController.class).getAll()).withSelfRel()
        );
    }

    @Operation(summary = "Обновить пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PutMapping("/{id}")
    public EntityModel<UserDto> update(
            @PathVariable int id,
            @RequestBody UserDto dto
    ) {
        return toModel(userService.update(id, dto));
    }

    @Operation(summary = "Удалить пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь удалён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        userService.delete(id);
    }

    private EntityModel<UserDto> toModel(UserDto user) {
        Integer id = user.getId();

        return EntityModel.of(
                user,
                linkTo(methodOn(UserController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAll()).withRel("all-users"),
                linkTo(methodOn(UserController.class).update(id, user)).withRel("update"),
                linkTo(UserController.class).slash(id).withRel("delete")
        );
    }
}