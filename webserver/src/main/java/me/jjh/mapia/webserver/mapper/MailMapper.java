package me.jjh.mapia.webserver.mapper;

import me.jjh.mapia.webserver.dto.mail.MailDTO;
import me.jjh.mapia.webserver.dto.mail.MailTplDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * packageName    : me.jjh.mapia.mapper
 * fileName       : mailMapper
 * author         : JJH
 * date           : 2025-02-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-24        JJH       최초 생성
 */

@Mapper
public interface MailMapper {

    /* SELECT */
    MailTplDTO findMailTplByKey(String mailKey);

    /* INSERT */
    void insertMail(MailDTO mailDTO);

    /* UPDATE */
    /* DELETE */




}
