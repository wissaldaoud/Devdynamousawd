package com.pi.trainingprogram.service;

import com.pi.trainingprogram.entities.Module;
import com.pi.trainingprogram.repository.TrainingModuleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final TrainingModuleRepository moduleRepository;

    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    public Module getModuleById(int id) {
        return moduleRepository.findById(id).orElse(null);
    }

    public Module createModule(Module module) {
        return moduleRepository.save(module);
    }

    public Module updateModule(int id, Module module) {
        if (moduleRepository.existsById(id)) {
            return moduleRepository.save(module);
        }
        throw new EntityNotFoundException("Module not found");
    }

    public void deleteModule(int id) {
        moduleRepository.deleteById(id);
    }
}
