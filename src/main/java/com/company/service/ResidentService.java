package com.company.service;

import com.company.adaptor.database.form.ResidentForm;
import com.company.adaptor.database.repository.ApartmentRepository;
import com.company.adaptor.database.repository.ResidentRepository;
import com.company.domain.entity.Apartment;
import com.company.domain.entity.Resident;
import com.company.domain.entity.Resident.ResidentStatus;
import com.company.domain.exception.ResidentServiceException;
import com.company.domain.exception.ResourceNotFoundException;
import com.company.presentation.rest.resident.response.ResidentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
            throw new ResidentServiceException("Email đã tồn tại");
        }
        if (residentRepository.existsByIdCard(form.getIdCard())) {
            throw new ResidentServiceException("Chứng minh thư đã tồn tại");
        }

        Resident resident = new Resident();
        resident.setName(form.getName());
        resident.setEmail(form.getEmail());
        resident.setPhone(form.getPhoneNumber());
        resident.setIdCard(form.getIdCard());
        resident.setBirthYear(form.getBirthYear());

        // Kiểm tra và gán giá trị cho gender
        Resident.ResidentGender gender = Resident.ResidentGender.toEnum(form.getGender());
        if (gender == null) {
            throw new ResidentServiceException("Giới tính không hợp lệ");
        }
        resident.setGender(gender);

        // Gán giá trị cho movedInDate, mặc định là ngày hiện tại nếu không có giá trị
        if (form.getMovedInDate() == null) {
            resident.setMovedInDate(LocalDate.now());
        } else {
            resident.setMovedInDate(form.getMovedInDate());
        }

        // Tìm và liên kết với căn hộ
        Apartment apartment = apartmentRepository.findById(form.getApartmentId())
                .orElseThrow(() -> new ResidentServiceException("Không tìm thấy căn hộ"));
        resident.setApartment(apartment);

        // Lưu cư dân vào cơ sở dữ liệu
        residentRepository.save(resident);

        return convertToDTO(resident);
    }

    public ResidentDTO updateResidentInfo(Integer residentId, ResidentForm form) throws Exception {
        // Tìm cư dân theo ID
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new ResidentServiceException("Không tìm thấy cư dân"));

        // Cập nhật số điện thoại, email, và tên nếu có trong form
        if (form.getPhoneNumber() != null) {
            resident.setPhone(form.getPhoneNumber());
        }
        if (form.getEmail() != null) {
            resident.setEmail(form.getEmail());
        }
        if (form.getName() != null) {
            resident.setName(form.getName());
        }

        // Nếu thông tin cho việc "bớt người ở" được cung cấp (movedOutDate)
        if (form.getMovedOutDate() != null) {
            resident.setMovedOutDate(form.getMovedOutDate());

            // Tìm và cập nhật số lượng cư dân trong căn hộ
            Apartment apartment = resident.getApartment();

            // Tính số lượng cư dân hiện tại trong căn hộ
            long currentOccupantsCount = residentRepository.countByApartmentIdAndMovedOutDateIsNull(apartment.getId());

            // Nếu cư dân chuyển đi, không cần giảm số lượng, vì chúng ta tính toán trực
            // tiếp
            if (resident.getMovedOutDate() == null) {
                resident.setMovedOutDate(form.getMovedOutDate());
            }

            // Lưu cư dân sau khi cập nhật thông tin
            residentRepository.save(resident);
        }

        return convertToDTO(resident);
    }

    @Override
    public void deleteResidentById(Integer residentId) {
        residentRepository.deleteById(residentId);
    }

    @Override
    public ResidentDTO moveOutResident(Integer residentId, LocalDate movedOutDate) throws Exception {
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new ResidentServiceException("Không tìm thấy cư dân"));

        // Kiểm tra nếu cư dân đã có ngày chuyển đi
        if (resident.getMovedOutDate() != null) {
            throw new ResidentServiceException("Cư dân đã dọn ra trước đó");
        }

        // Cập nhật ngày chuyển đi và trạng thái
        resident.setMovedOutDate(movedOutDate);
        resident.setStatus(ResidentStatus.INACTIVE); // Cập nhật trạng thái thành INACTIVE

        // Lưu cư dân sau khi cập nhật thông tin
        residentRepository.save(resident);

        return convertToDTO(resident);
    }

    @Override
    public ResidentDTO moveInResident(Integer residentId, Integer apartmentId, ResidentForm form) throws Exception {
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new ResidentServiceException("Không tìm thấy cư dân"));

        // Cập nhật thông tin căn hộ mới
        if (apartmentId != null) {
            Apartment apartment = apartmentRepository.findById(apartmentId)
                    .orElseThrow(() -> new ResidentServiceException("Không tìm thấy căn hộ"));
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

    // Hàm xử lý soft delete
    public Resident softDeleteResident(Integer residentId) {
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new ResourceNotFoundException("Resident not found"));

        resident.setStatus(ResidentStatus.INACTIVE);
        resident.setMovedOutDate(LocalDate.now());

        return residentRepository.save(resident);
    }

    public List<Resident> getActiveResidents() {
        return residentRepository.findByStatus(ResidentStatus.ACTIVE);
    }
}
