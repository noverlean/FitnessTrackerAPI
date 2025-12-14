package noverlin.fitness.service;

import noverlin.fitness.dto.workout.WorkoutRequest;
import noverlin.fitness.dto.workout.WorkoutResponse;
import noverlin.fitness.exceptions.custom.access.UserHasNotAccessRulesException;
import noverlin.fitness.exceptions.custom.notFound.UserNotFoundException;
import noverlin.fitness.exceptions.custom.notFound.WorkoutNotFoundException;
import noverlin.fitness.mapper.WorkoutMapper;
import noverlin.fitness.model.User;
import noverlin.fitness.model.Workout;
import noverlin.fitness.model.WorkoutType;
import noverlin.fitness.repository.UserRepository;
import noverlin.fitness.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkoutMapper workoutMapper;

    @InjectMocks
    private WorkoutService workoutService;

    private User user;
    private Workout workout;
    private WorkoutResponse workoutResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        workout = new Workout()
                .setId(10L)
                .setUser(user)
                .setTitle("Morning Run")
                .setType(WorkoutType.CARDIO)
                .setDate(Instant.now())
                .setDuration(30L)
                .setCalories(200L);

        workoutResponse = new WorkoutResponse();
        workoutResponse.setId(10L);
        workoutResponse.setTitle("Morning Run");

        SecurityContextHolder.clearContext();
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("testuser")
                .password("password")
                .authorities("ROLE_USER")
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void findById_ShouldReturnWorkoutResponse() {
        when(workoutRepository.findByIdForUserWithUsername(10L, "testuser"))
                .thenReturn(Optional.of(workout));
        when(workoutMapper.toDto(workout)).thenReturn(workoutResponse);

        WorkoutResponse result = workoutService.findById(10L);

        assertEquals("Morning Run", result.getTitle());
        verify(workoutRepository).findByIdForUserWithUsername(10L, "testuser");
    }

    @Test
    void findById_ShouldThrowWorkoutNotFoundException() {
        when(workoutRepository.findByIdForUserWithUsername(10L, "testuser"))
                .thenReturn(Optional.empty());

        assertThrows(WorkoutNotFoundException.class,
                () -> workoutService.findById(10L));
    }

    @Test
    void createForUser_ShouldSaveAndReturnResponse() {
        WorkoutRequest request = new WorkoutRequest();
        request.setTitle("Morning Run");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(workoutMapper.toModel(request)).thenReturn(workout);
        when(workoutMapper.toDto(workout)).thenReturn(workoutResponse);

        WorkoutResponse result = workoutService.createForUser(request);

        assertEquals("Morning Run", result.getTitle());
        verify(workoutRepository).save(workout);
    }

    @Test
    void createForUser_ShouldThrowUserNotFoundException() {
        WorkoutRequest request = new WorkoutRequest();
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> workoutService.createForUser(request));
    }

    @Test
    void update_ShouldUpdateAndReturnResponse() {
        WorkoutRequest request = new WorkoutRequest();
        request.setTitle("Updated Run");

        when(workoutRepository.findById(10L)).thenReturn(Optional.of(workout));
        when(workoutMapper.toDto(workout)).thenReturn(workoutResponse);

        WorkoutResponse result = workoutService.update(10L, request);

        verify(workoutMapper).updateModelFromDto(request, workout);
        verify(workoutRepository).save(workout);
        assertEquals("Morning Run", result.getTitle()); // mapped response
    }

    @Test
    void update_ShouldThrowWorkoutNotFoundException() {
        WorkoutRequest request = new WorkoutRequest();
        when(workoutRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(WorkoutNotFoundException.class,
                () -> workoutService.update(10L, request));
    }

    @Test
    void update_ShouldThrowUserHasNotAccessRulesException() {
        User otherUser = new User();
        otherUser.setUsername("other");
        workout.setUser(otherUser);

        WorkoutRequest request = new WorkoutRequest();
        when(workoutRepository.findById(10L)).thenReturn(Optional.of(workout));

        assertThrows(UserHasNotAccessRulesException.class,
                () -> workoutService.update(10L, request));
    }

    @Test
    void delete_ShouldDeleteWorkout() {
        when(workoutRepository.findById(10L)).thenReturn(Optional.of(workout));

        workoutService.delete(10L);

        verify(workoutRepository).delete(workout);
    }

    @Test
    void delete_ShouldThrowWorkoutNotFoundException() {
        when(workoutRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(WorkoutNotFoundException.class,
                () -> workoutService.delete(10L));
    }

    @Test
    void delete_ShouldThrowUserHasNotAccessRulesException() {
        User otherUser = new User();
        otherUser.setUsername("other");
        workout.setUser(otherUser);

        when(workoutRepository.findById(10L)).thenReturn(Optional.of(workout));

        assertThrows(UserHasNotAccessRulesException.class,
                () -> workoutService.delete(10L));
    }
}

