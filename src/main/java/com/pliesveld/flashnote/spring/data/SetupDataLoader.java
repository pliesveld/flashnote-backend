package com.pliesveld.flashnote.spring.data;

import com.pliesveld.flashnote.domain.AccountRole;
import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.repository.CategoryRepository;
import com.pliesveld.flashnote.repository.StudentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.pliesveld.flashnote.domain.AccountRole.*;



@Component
@Transactional
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = LogManager.getLogger();

    private boolean alreadySetup = false;

    final private static String DEFAULT_PASSWORD = "password";

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup)
            return;

        createStudentIfNotFound("new@example.com",     ROLE_ACCOUNT,    "new");
        createStudentIfNotFound("student@example.com", ROLE_USER,       "basic");
        createStudentIfNotFound("premium@example.com", ROLE_PREMIUM,    "premium");
        createStudentIfNotFound("mod@example.com",     ROLE_MODERATOR,  "moderator");
        createStudentIfNotFound("admin@example.com",   ROLE_ADMIN,      "admin");

        Category test_category = createCategoryIfNotFound("TEST CATEGORY", "A sample container for testing.");
        Category test_category_nested = createCategoryIfNotFound("TEST SUB CATEGORY", "A sample sub-category for testing.");
        createCategoryRelationship(test_category,test_category_nested);
        alreadySetup = true;
    }

    @Transactional
    private Student createStudentIfNotFound(String email, AccountRole role, String name) {
        Student student = studentRepository.findOneByEmail(email);
        if ( student == null )
        {
            student = new Student();
            student.setEmail(email);
            student.setRole(role);
            student.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
            student.setName(name);
            studentRepository.save(student);
        }

        return student;
    }

    @Transactional
    private void createCategoryRelationship(Category parent, Category child) {
        parent = categoryRepository.findOne(parent.getId());
        child = categoryRepository.findOne(child.getId());

        if (!child.isParent(parent))
            parent.addChildCategory(child);

        categoryRepository.save(child);
    }

    @Transactional
    private Category createCategoryIfNotFound(String name, String description)
    {
        Category category = categoryRepository.findOneByNameEquals(name);

        if ( category == null )
        {
            category = new Category();
            category.setName(name);
            category.setDescription(description);
            categoryRepository.save(category);
        }

        return category;
    }

/*
    @Transactional
    private final Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private final Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
    */

}
