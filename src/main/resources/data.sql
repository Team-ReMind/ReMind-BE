use
remind;

insert into member(id, auth_id, name, age, gender, email, phone_number, profile_image_url, is_onboarding_finished,
                   registration_token, roles_type)
VALUES (1, 1, '이상민', 27, 'men', '1234@gmail.com', '010-0000-0000', 'test-image-url', true, 'test-token', 'ROLE_USER');