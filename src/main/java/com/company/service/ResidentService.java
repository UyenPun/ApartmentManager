package com.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.dto.ResidentDTO;
import com.company.entity.Apartment;
import com.company.entity.Resident;
import com.company.form.ResidentForm;
import com.company.repository.ApartmentRepository;
import com.company.repository.ResidentRepository;

import java.time.LocalDate;

@Service
public class ResidentService implements IResidentService {

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Override
    public ResidentDTO addResident(ResidentForm form) throws Exception {
        // Kiểm tra email và chứng minh thư đã tồn tại
        if (residentRepository.existsByEmail(form.getEmail())) {
            throw new Exception("Email đã tồn tại");
        }
        if (residentRepository.existsByIdCard(form.getIdCard())) {
            throw new Exception("Chứng minh thư đã tồn tại");
        }

        Resident resident = new Resident();
        resident.setName(form.getName());
        resident.setEmail(form.getEmail());
        resident.setPhone(form.getPhoneNumber());
        resident.setIdCard(form.getIdCard());
        resident.setBirthYear(form.getBirthYear());
        resident.setGender(Resident.ResidentGender.toEnum(form.getGender()));
        resident.setMovedInDate(form.getMovedInDate());

        // Tìm và liên kết với căn hộ
        Apartment apartment = apartmentRepository.findById(form.getApartmentId())
                .orElseThrow(() -> new Exception("Không tìm thấy căn hộ"));
        resident.setApartment(apartment);

        // Lưu cư dân vào cơ sở dữ liệu
        residentRepository.save(resident);

        return convertToDTO(resident);
    }

    @Override
    public ResidentDTO moveOutResident(Integer residentId, LocalDate movedOutDate) throws Exception {
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new Exception("Không tìm thấy cư dân"));

        // Kiểm tra nếu cư dân đã ở một căn hộ khác
        if (resident.getMovedOutDate() != null) {
            throw new Exception("Cư dân đã dọn ra trước đó");
        }

        resident.setMovedOutDate(movedOutDate);
        residentRepository.save(resident);

        return convertToDTO(resident);
    }

    @Override
    public ResidentDTO moveInResident(Integer residentId, Integer apartmentId, ResidentForm form) throws Exception {
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new Exception("Không tìm thấy cư dân"));

        // Cập nhật thông tin căn hộ mới
        if (apartmentId != null) {
            Apartment apartment = apartmentRepository.findById(apartmentId)
                    .orElseThrow(() -> new Exception("Không tìm thấy căn hộ"));
            resident.setApartment(apartment);
            resident.setMovedInDate(LocalDate.now());
        }

        // Cập nhật các thông tin khác nếu có
        if (form.getPhoneNumber() != null) {
            resident.setPhone(form.getPhoneNumber());
        }
        if (form.getEmail() != null) {
            resident.setEmail(form.getEmail());
        }
        // Các trường khác có thể thêm vào tương tự

        residentRepository.save(resident);
        return convertToDTO(resident);
    }

    private ResidentDTO convertToDTO(Resident resident) {
        return new ResidentDTO(resident.getId(), resident.getName(), resident.getEmail(), resident.getPhone(),
                resident.getIdCard(), resident.getBirthYear(), resident.getGender(), resident.getApartment().getId(),
                resident.getMovedInDate(), resident.getMovedOutDate());
    }
}
