package noverlin.fitness.exceptions.custom;

public class WorkoutWasNotFound extends RuntimeException {
    public WorkoutWasNotFound() {
        super("Тренировки не существует");
    } //todo: добавить обработку ошибки с корректным кодом вместо 500
    public WorkoutWasNotFound(String message) {
        super(message);
    }
}

