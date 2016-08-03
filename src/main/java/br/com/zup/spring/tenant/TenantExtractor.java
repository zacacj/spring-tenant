package br.com.zup.spring.tenant;

public interface TenantExtractor {

    String extractFrom(String slug);

    class TenantExtractorNotFoundException extends RuntimeException {
        public TenantExtractorNotFoundException(String message) {
            super(message);
        }
    }
}
