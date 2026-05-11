package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {

    private final VikingService vikingService;
    private final VikingListener vikingListener;

    public VikingController(VikingService vikingService, VikingListener vikingListener) {
        this.vikingService = vikingService;
        this.vikingListener = vikingListener;
    }

    @GetMapping
    @Operation(summary = "Получить список созданных викингов",
            operationId = "getAllVikings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<Viking> getAllVikings() {
        System.out.println("GET /api/vikings called");
        return vikingService.findAll();
    }

    @GetMapping("/test")
    @Operation(summary = "Получить список тестовых викингов",
            operationId = "getTest")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<String> test() {
        System.out.println("GET /api/vikings/test called");
        return List.of("Ragnar", "Bjorn");
    }

    @PostMapping
    @Operation(summary = "Добавить конкретного викинга",
            operationId = "addViking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно добавлен")
    })
    public Viking addViking(@RequestBody Viking viking) {
        System.out.println("POST /api/vikings called");
        Viking saved = vikingService.addViking(viking);
        vikingListener.addViking(saved);
        return saved;
    }

    @PostMapping("/post")
    @Operation(summary = "Создать викинга со случайными параметрами",
            operationId = "post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно создан")
    })
    public Viking addRandomViking() {
        System.out.println("POST /api/vikings/post called");
        Viking saved = vikingService.createRandomViking();
        vikingListener.addViking(saved);
        return saved;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Перезаписать параметры конкретного викинга",
            operationId = "updateViking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Викинг не найден")
    })
    public Viking updateViking(@PathVariable int id, @RequestBody Viking viking) {
        System.out.println("PUT /api/vikings/" + id + " called");

        try {
            Viking updated = vikingService.updateById(id, viking);
            vikingListener.updateViking(updated);
            return updated;
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить викинга из таблицы",
            operationId = "deleteViking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно удален")
    })
    public void deleteViking(@PathVariable int id) {
        System.out.println("DELETE /api/vikings/" + id + " called");
        vikingService.deleteById(id);
        vikingListener.deleteVikingById(id);
    }
}

