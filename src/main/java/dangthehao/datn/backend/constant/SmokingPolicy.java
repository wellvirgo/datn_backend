package dangthehao.datn.backend.constant;

import lombok.Getter;

@Getter
public enum SmokingPolicy {
  NOT_IN_ROOM("Not in room"),
  NO_SMOKING("No smoking"),
  IN_ALLOWED_AREA("In allowed area"),
  ;
  private final String value;

  SmokingPolicy(String value) {
    this.value = value;
  }

}
