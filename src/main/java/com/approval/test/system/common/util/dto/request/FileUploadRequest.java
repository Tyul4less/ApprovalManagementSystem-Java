package com.approval.test.system.common.util.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@ToString
@Setter
@Getter
@Alias("FileUploadRequest")
public class FileUploadRequest {

    private int seq;
    private String fileOriginalName;
    private String fileRandomName;
    private String fileUrl;
}