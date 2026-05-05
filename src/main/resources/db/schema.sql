-- Create students table
CREATE TABLE IF NOT EXISTS students (
    id BIGSERIAL PRIMARY KEY,
    student_code VARCHAR(20) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    major VARCHAR(100),
    academic_year INT,
    total_credits INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create courses table
CREATE TABLE IF NOT EXISTS courses (
    id BIGSERIAL PRIMARY KEY,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(150) NOT NULL,
    credits INT NOT NULL,
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create course_sections table with integrated scheduling and prerequisites
CREATE TABLE IF NOT EXISTS course_sections (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL,
    section_code VARCHAR(20) NOT NULL,
    max_slots INT NOT NULL,
    current_slots INT DEFAULT 0,
    semester INT NOT NULL,
    year INT NOT NULL,
    day_of_week VARCHAR(10),
    start_period INT,
    end_period INT,
    room VARCHAR(50),
    prerequisite_course_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (prerequisite_course_id) REFERENCES courses(id) ON DELETE SET NULL,
    UNIQUE (course_id, section_code),
    CHECK (start_period < end_period)
);

-- Create enrollments table
CREATE TABLE IF NOT EXISTS enrollments (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    section_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'REGISTERED',
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (section_id) REFERENCES course_sections(id) ON DELETE CASCADE,
    UNIQUE (student_id, section_id)
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_student_code ON students(student_code);
CREATE INDEX IF NOT EXISTS idx_student_email ON students(email);
CREATE INDEX IF NOT EXISTS idx_course_code ON courses(course_code);
CREATE INDEX IF NOT EXISTS idx_course_section ON course_sections(course_id);
CREATE INDEX IF NOT EXISTS idx_enrollment_student ON enrollments(student_id);
CREATE INDEX IF NOT EXISTS idx_enrollment_section ON enrollments(section_id);
CREATE INDEX IF NOT EXISTS idx_enrollment_status ON enrollments(status);
CREATE INDEX IF NOT EXISTS idx_schedule_section ON course_schedules(section_id);
