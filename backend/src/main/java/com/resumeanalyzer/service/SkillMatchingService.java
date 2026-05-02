package com.resumeanalyzer.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SkillMatchingService {

    private static final Map<String, List<String>> JOB_ROLE_SKILLS = new LinkedHashMap<>();

    static {
        JOB_ROLE_SKILLS.put("Java Developer", List.of(
                "Java", "Spring Boot", "Spring MVC", "Spring Framework", "Hibernate", "JPA",
                "Maven", "Gradle", "SQL", "REST API", "Git", "Docker", "JUnit", "Mockito",
                "PostgreSQL", "MySQL", "Microservices", "Design Patterns", "OOP", "Multithreading"
        ));

        JOB_ROLE_SKILLS.put("Frontend Developer", List.of(
                "React", "JavaScript", "TypeScript", "HTML", "CSS", "Redux",
                "Node.js", "Git", "REST API", "Webpack", "npm", "Jest",
                "Responsive Design", "SASS", "Tailwind CSS", "Vue.js",
                "Angular", "GraphQL", "Axios", "Unit Testing"
        ));

        JOB_ROLE_SKILLS.put("Full Stack Developer", List.of(
                "Java", "Spring Boot", "React", "JavaScript", "TypeScript",
                "SQL", "Git", "Docker", "REST API", "HTML", "CSS",
                "PostgreSQL", "MongoDB", "Node.js", "Microservices",
                "AWS", "CI/CD", "Maven", "JUnit", "Redux"
        ));

        JOB_ROLE_SKILLS.put("Data Scientist", List.of(
                "Python", "Machine Learning", "Deep Learning", "TensorFlow", "PyTorch",
                "SQL", "Pandas", "NumPy", "Scikit-learn", "Data Analysis",
                "Statistics", "Matplotlib", "R", "Jupyter", "Feature Engineering",
                "NLP", "Computer Vision", "Keras", "Data Visualization", "Big Data"
        ));

        JOB_ROLE_SKILLS.put("DevOps Engineer", List.of(
                "Docker", "Kubernetes", "Jenkins", "CI/CD", "AWS", "Azure",
                "Linux", "Shell Scripting", "Git", "Terraform", "Ansible",
                "Monitoring", "Prometheus", "Grafana", "Python",
                "Nginx", "Load Balancing", "Networking", "Security", "Helm"
        ));

        JOB_ROLE_SKILLS.put("Python Developer", List.of(
                "Python", "Django", "Flask", "FastAPI", "SQL", "REST API",
                "Git", "Docker", "PostgreSQL", "Redis", "Celery",
                "OOP", "Design Patterns", "Unit Testing", "Pytest",
                "SQLAlchemy", "AWS", "Microservices", "MongoDB", "GraphQL"
        ));

        JOB_ROLE_SKILLS.put("Android Developer", List.of(
                "Android", "Java", "Kotlin", "Android SDK", "XML",
                "MVVM", "MVP", "Retrofit", "Room", "LiveData",
                "ViewModel", "Jetpack Compose", "Git", "SQLite", "REST API",
                "Firebase", "Unit Testing", "Material Design", "Gradle", "Coroutines"
        ));

        JOB_ROLE_SKILLS.put("Cloud Engineer", List.of(
                "AWS", "Azure", "GCP", "Docker", "Kubernetes", "Terraform",
                "Linux", "Python", "Shell Scripting", "CI/CD", "Jenkins",
                "Networking", "Security", "IAM", "VPC", "S3",
                "EC2", "Lambda", "Load Balancing", "CloudFormation"
        ));
    }

    public List<String> getAvailableJobRoles() {
        return new ArrayList<>(JOB_ROLE_SKILLS.keySet());
    }

    public List<String> extractSkillsFound(String text, String jobRole) {
        String lower = text.toLowerCase();
        return requiredFor(jobRole).stream()
                .filter(s -> lower.contains(s.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<String> extractMissingSkills(String text, String jobRole) {
        String lower = text.toLowerCase();
        return requiredFor(jobRole).stream()
                .filter(s -> !lower.contains(s.toLowerCase()))
                .collect(Collectors.toList());
    }

    public double calculateScore(int found, String jobRole) {
        int total = requiredFor(jobRole).size();
        if (total == 0) return 0.0;
        return Math.round((found / (double) total) * 100 * 10.0) / 10.0;
    }

    public List<String> generateSuggestions(List<String> missing, String jobRole, double score) {
        List<String> suggestions = new ArrayList<>();

        if (score >= 80) {
            suggestions.add("Excellent match! Your resume is well-aligned with the " + jobRole + " role.");
        } else if (score >= 60) {
            suggestions.add("Strong profile for " + jobRole + ". A few more skills will make you a top candidate.");
        } else if (score >= 40) {
            suggestions.add("Good start! Add more relevant technical skills to strengthen your " + jobRole + " profile.");
        } else {
            suggestions.add("Your resume needs improvement for " + jobRole + ". Focus on building core skills first.");
        }

        if (!missing.isEmpty()) {
            int top = Math.min(missing.size(), 5);
            suggestions.add("Priority skills to add: " + String.join(", ", missing.subList(0, top)) + ".");
        }

        suggestions.add("Quantify achievements with metrics (e.g., 'Improved API response time by 40%').");
        suggestions.add("Add links to GitHub projects or a portfolio to showcase practical experience.");

        if (score < 50) {
            suggestions.add("Consider online courses on Udemy, Coursera, or Pluralsight to fill skill gaps.");
        }

        suggestions.add("Use ATS-friendly formatting: clear section headers, standard fonts, no tables in header.");

        return suggestions;
    }

    private List<String> requiredFor(String jobRole) {
        return JOB_ROLE_SKILLS.getOrDefault(jobRole, Collections.emptyList());
    }
}
