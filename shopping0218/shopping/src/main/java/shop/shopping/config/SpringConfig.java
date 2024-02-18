package shop.shopping.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shop.shopping.repository.ItemRepository.ItemImgRepository;
import shop.shopping.repository.ItemRepository.ItemRepository;
import shop.shopping.repository.MemberRepository.MemberRepository;
import shop.shopping.service.ItemService.ItemImgService;
import shop.shopping.service.ItemService.ItemService;
import shop.shopping.service.MemberService.MemberService;

@Configuration
public class SpringConfig implements WebMvcConfigurer {

    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Value("${uploadPath}")
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }

}
