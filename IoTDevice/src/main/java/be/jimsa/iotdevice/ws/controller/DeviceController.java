package be.jimsa.iotdevice.ws.controller;

import be.jimsa.iotdevice.config.exception.AppException;
import be.jimsa.iotdevice.utility.constant.DeviceConstants;
import be.jimsa.iotdevice.ws.model.dto.DeviceDto;
import be.jimsa.iotdevice.ws.model.dto.ResponseDto;
import be.jimsa.iotdevice.ws.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping(path = "/v1/devices", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    public ResponseEntity<ResponseDto> createNewDevice(
            @RequestBody DeviceDto deviceDto
    ){
        Optional<DeviceDto> result = deviceService.createNewDevice(deviceDto);

        return result
                .map(dto -> ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(
                                    ResponseDto.builder()
                                            .action(true)
                                            .timestamp(LocalDateTime.now())
                                            .result(dto)
                                            .build()
                            )
                )
                .orElseThrow(() -> new AppException(DeviceConstants.EXCEPTION_CAN_NOT_CREATE));

    }
}
