package dangthehao.datn.backend.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = DateRangeValidator.class)
public @interface DateRange {
  String message() default "Invalid date range";

  String startDate() default "checkIn";

  String endDate() default "checkOut";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
