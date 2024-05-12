use
remind;

INSERT INTO member(id, auth_id, name, age, gender, email, phone_number, profile_image_url, is_onboarding_finished,
                   registration_token, roles_type, protector_phone_number, city, district, center_name)
VALUES (1, 1, '이상민', 27, 'men', '1234@gmail.com', '010-0000-0000', 'test-image-url', true, 'test-token', 'ROLE_USER', 
        '010-1111-2222', 'Seoul', 'Gangnam', 'Center A');
