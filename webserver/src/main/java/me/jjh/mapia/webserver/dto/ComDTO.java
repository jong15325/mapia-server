package me.jjh.mapia.webserver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * packageName    : me.jjh.mapia.dto
 * fileName       : ComDTO
 * author         : JJH
 * date           : 2025-03-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-03-16        JJH       최초 생성
 */

@NoArgsConstructor
@Getter
@Setter
public abstract class ComDTO {

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt; // 생성일
    private Long createdIdx; // 생성자 idx
    private String createdId; // 생성자 id
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt; // 수정일
    private Long updatedIdx; // 수정자 idx
    private String updatedId; // 수정자 id

    public String formatCreatedAt(String pattern) {
        if (createdAt != null) {
            return createdAt.format(DateTimeFormatter.ofPattern(pattern));
        }
        return "";
    }

    public String formatUpdatedAt(String pattern) {
        if (updatedAt != null) {
            return updatedAt.format(DateTimeFormatter.ofPattern(pattern));
        }
        return "";
    }

    public String getSmartDateFormat(String timePattern, String datePattern) {
        if (createdAt != null) {
            LocalDate today = LocalDate.now();
            LocalDate createdDate = createdAt.toLocalDate();

            if (today.equals(createdDate)) {
                return createdAt.format(DateTimeFormatter.ofPattern(timePattern));
            } else {
                return createdAt.format(DateTimeFormatter.ofPattern(datePattern));
            }
        }
        return "";
    }
}
