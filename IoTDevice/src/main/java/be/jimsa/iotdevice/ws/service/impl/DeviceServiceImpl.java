package be.jimsa.iotdevice.ws.service.impl;

import be.jimsa.iotdevice.utility.constant.DeviceConstants;
import be.jimsa.iotdevice.ws.model.dto.DeviceDto;
import be.jimsa.iotdevice.ws.model.entity.DeviceEntity;
import be.jimsa.iotdevice.ws.repository.DeviceRepository;
import be.jimsa.iotdevice.ws.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
//    private final PublicIdGenerator publicIdGenerator;
//    private final ModelMapper modelMapper;

    @Override
    public Optional<DeviceDto> createNewDevice(DeviceDto deviceDto) {
//        return Optional.of(
//                modelMapper.mapToDto(
//                        deviceRepository.save(
//                                modelMapper.mapToEntity(deviceDto)
//                        )
//                )
//        );
        return Optional.empty();
    }
}
