
package org.sistemas.ticketsystemapp.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttachmentDTO {

    private Long id;
    private String fileName;
    private String fileType;
    private String fileUrl;
}
