package dangthehao.datn.backend.constant;

import lombok.Getter;

@Getter
public enum OccupancyStatus {
  LOW("Thấp", "#FF4D4F"),
  AVERAGE("Trung bình", "#FAAD14"),
  HIGH("Cao", "#52C41A"),
  EXCELLENT("Rất cao", "#1890FF");

  private final String label;
  private final String color;

  OccupancyStatus(String label, String color) {
    this.label = label;
    this.color = color;
  }

  public static OccupancyStatus fromPercentage(Long percentage) {
    if (percentage == null || percentage < 40) return LOW;
    if (percentage < 75) return AVERAGE;
    if (percentage < 95) return HIGH;
    return EXCELLENT;
  }
}
