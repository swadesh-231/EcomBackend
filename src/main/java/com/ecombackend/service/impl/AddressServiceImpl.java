package com.ecombackend.service.impl;

import com.ecombackend.dto.AddressDTO;
import com.ecombackend.entity.Address;
import com.ecombackend.entity.User;
import com.ecombackend.exception.ResourceNotFoundException;
import com.ecombackend.repository.AddressRepository;
import com.ecombackend.repository.UserRepository;
import com.ecombackend.service.AddressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(user);

        if (user.getAddresses() == null) {
            user.setAddresses(new ArrayList<>());
        }
        user.getAddresses().add(address);

        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO getAddressesById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        List<Address> addresses = user.getAddresses();
        if (addresses == null || addresses.isEmpty()) {
            return Collections.emptyList();
        }
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        existingAddress.setCity(addressDTO.getCity());
        existingAddress.setPincode(addressDTO.getPincode());
        existingAddress.setState(addressDTO.getState());
        existingAddress.setCountry(addressDTO.getCountry());
        existingAddress.setStreet(addressDTO.getStreet());
        existingAddress.setBuildingName(addressDTO.getBuildingName());

        Address updatedAddress = addressRepository.save(existingAddress);

        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    @Transactional
    public String deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        User user = address.getUser();
        if (user != null && user.getAddresses() != null) {
            user.getAddresses().removeIf(a -> a.getAddressId().equals(addressId));
        }

        addressRepository.delete(address);

        return "Address deleted successfully with addressId: " + addressId;
    }
}