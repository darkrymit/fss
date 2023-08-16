package ua.edu.nau.darkrymit.fss.shared;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface InstantMapper {

  default Long toLong(java.time.Instant instant) {
    return instant.toEpochMilli();
  }

  default java.time.Instant toInstant(Long epochMilli) {
    return java.time.Instant.ofEpochMilli(epochMilli);
  }
}
