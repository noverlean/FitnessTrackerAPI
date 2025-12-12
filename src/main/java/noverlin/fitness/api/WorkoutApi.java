package noverlin.fitness.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import noverlin.fitness.dto.workout.WorkoutRequest;
import noverlin.fitness.dto.workout.WorkoutResponse;
import noverlin.fitness.dto.workout.WorkoutSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/workouts")
public interface WorkoutApi {

    @Operation(
            summary = "Расширенный поиск",
            description = "возвращает список пользовательских тренировок в пагирированном формате с возможностью " +
                    "фильтрации по типу нагрузок, дате или продолжительности, а так же в отсортированном формате"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    Page<WorkoutResponse> getAll(
            WorkoutSearchRequest searchRequest,
            @PageableDefault(size = 5) Pageable pageable
    );

    @Operation(
            summary = "Получить тренировку по ID",
            description = "Возвращает тренировку по ее идентификатору учитывая принадлежность авторизованному пользователю"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/{id}")
    WorkoutResponse getWorkoutById(@PathVariable Long id);

    @Operation(
            summary = "Создать тренировку",
            description = "Создает тренировку авторизованному пользователю"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PostMapping
    WorkoutResponse createWorkout(@RequestBody WorkoutRequest workoutRequest);

    @Operation(
            summary = "Обновить данные тренировки",
            description = "Обновить данные тренировки для авторизованного пользователя по ее идентификатору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping("/{id}")
    WorkoutResponse updateWorkout(@PathVariable Long id, @RequestBody WorkoutRequest workoutRequest);

    @Operation(
            summary = "Удалить тренировку",
            description = "Удаляет тренировку для авторизованного пользователя по ее идентификатору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteWorkout(@PathVariable Long id);
}
