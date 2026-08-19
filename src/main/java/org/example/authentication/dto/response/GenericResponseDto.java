
package org.example.authentication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponseDto {

    private boolean success;
    private String errorCode;
    private String errorMessage;


   public void basarili(String errorMessage) {
        this.success = true;
        this.errorCode = "0";
        this.errorMessage = errorMessage;
    }

    public void hatali(String errorCode, String errorMessage) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    public void loginException(String errorCode, String errorMessage) {
        this.success = false;
        this.errorCode = "0";
        this.errorMessage = errorMessage;
    }

}