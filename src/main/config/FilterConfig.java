//@Configuration
//public class FilterConfig {
//
//    @Bean
//    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterRegistration(ApiKeyFilter filter) {
//        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(filter);
//        registrationBean.addUrlPatterns("/api/*"); // apply filter only to API endpoints
//        registrationBean.setOrder(1); // set filter order if multiple filters exist
//        return registrationBean;
//    }
//}