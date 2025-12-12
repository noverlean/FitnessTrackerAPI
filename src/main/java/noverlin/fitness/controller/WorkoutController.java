package noverlin.fitness.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import noverlin.fitness.dto.workout.WorkoutRequest;
import noverlin.fitness.dto.workout.WorkoutResponse;
import noverlin.fitness.dto.workout.WorkoutSearchRequest;
import noverlin.fitness.model.WorkoutType;
import noverlin.fitness.service.WorkoutService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RequiredArgsConstructor
@RestController
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

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
    public Page<WorkoutResponse> getAll(
            @AuthenticationPrincipal UserDetails userDetails,
            WorkoutSearchRequest searchRequest,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        String username = userDetails.getUsername();
        return workoutService.search(username, searchRequest, pageable);
    }

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
    public WorkoutResponse getWorkoutById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return workoutService.findById(id, username);
    }

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
    public WorkoutResponse createWorkout(@RequestBody WorkoutRequest workoutRequest, @AuthenticationPrincipal UserDetails userDetails) {
        return workoutService.createForUser(userDetails.getUsername(), workoutRequest);
    }

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
    public WorkoutResponse updateWorkout(@PathVariable Long id, @RequestBody WorkoutRequest workoutRequest, @AuthenticationPrincipal UserDetails userDetails) {
        return workoutService.update(id, userDetails.getUsername(), workoutRequest);
    }

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
    public ResponseEntity<Object> deleteWorkout(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        workoutService.delete(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
