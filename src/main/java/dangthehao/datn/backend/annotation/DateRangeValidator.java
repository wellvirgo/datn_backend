package dangthehao.datn.backend.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Slf4j
public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {
  private String startDateField;
  private String endDateField;
  private String message;

  @Override
  public void initialize(DateRange constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    this.startDateField = constraintAnnotation.startDate();
    this.endDateField = constraintAnnotation.endDate();
    this.message = constraintAnnotation.message();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    Object startDateObj = new BeanWrapperImpl(value).getPropertyValue(startDateField);
    Object endDateObj = new BeanWrapperImpl(value).getPropertyValue(endDateField);

    if (startDateObj == null || endDateObj == null) {
      return true;
    }

    LocalDate startDate = (LocalDate) startDateObj;
    LocalDate endDate = (LocalDate) endDateObj;

    long nights = ChronoUnit.DAYS.between(startDate, endDate);
    boolean isValid = nights >= 1 && nights <= 30;
    if (!isValid) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(message)
          .addPropertyNode(this.endDateField)
          .addConstraintViolation();
    }

    return isValid;
  }
}
