package be.jimsa.iotdevice.ws.service;

import be.jimsa.iotdevice.ws.model.dto.DeviceDto;

import java.util.Optional;

public interface DeviceService {
    Optional<DeviceDto> createNewDevice(DeviceDto deviceDto);
}
