package com.isi.gestionformation.Service;

import com.isi.gestionformation.model.Role;
import com.isi.gestionformation.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<Role> findAll() { return roleRepository.findAll(); }

    @Transactional(readOnly = true)
    public Optional<Role> findById(Long id) { return roleRepository.findById(id); }

    public Role create(Role role) {
        if (roleRepository.existsByNom(role.getNom())) {
            throw new RuntimeException("Rôle déjà existant : " + role.getNom());
        }
        return roleRepository.save(role);
    }

    public Role update(Long id, Role roleModifie) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rôle non trouvé : " + id));
        role.setNom(roleModifie.getNom());
        return roleRepository.save(role);
    }

    public void delete(Long id) {
        if (!roleRepository.existsById(id))
            throw new RuntimeException("Rôle non trouvé : " + id);
        roleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long count() { return roleRepository.count(); }
}