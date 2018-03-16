package eu.esens.espdvcd.validator;

public class ValidationResult {

    private final String id;
    private final String flag;
    private final String location;
    private final String test;
    private final String text;
    private final String role;
    private final String expectedValue;
    private final String providedValue;

    private ValidationResult(Builder builder) {
        this.id = builder.id;
        this.flag = builder.flag;
        this.location = builder.location;
        this.test = builder.test;
        this.text = builder.text;
        this.role = builder.role;
        this.expectedValue = builder.expectedValue;
        this.providedValue = builder.providedValue;
    }

    public String getFlag() {
        return flag;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getTest() {
        return test;
    }

    public String getText() {
        return text;
    }

    public String getRole() {
        return role;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public String getProvidedValue() {
        return providedValue;
    }

    public static class Builder {

        // mandatory params
        private final String id;
        private final String location;
        private final String text;
        // optional params
        private String flag;
        private String test;
        private String role;
        private String expectedValue;
        private String providedValue;

        public Builder(String id, String location, String text) {
            this.id = id;
            this.location = location;
            this.text = text;
        }

        public Builder flag(String flag) {
            this.flag = flag;
            return this;
        }

        public Builder test(String test) {
            this.test = test;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder expectedValue(String expectedValue) {
            this.expectedValue = expectedValue;
            return this;
        }

        public Builder providedValue(String providedValue) {
            this.providedValue = providedValue;
            return this;
        }

        public ValidationResult build() {
            return new ValidationResult(this);
        }

    }

    @Override
    public String toString() {
        return "ESPDFailedAssert{" +
                "id='" + id + '\'' +
                ", flag='" + flag + '\'' +
                ", location='" + location + '\'' +
                ", test='" + test + '\'' +
                ", text='" + text + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
