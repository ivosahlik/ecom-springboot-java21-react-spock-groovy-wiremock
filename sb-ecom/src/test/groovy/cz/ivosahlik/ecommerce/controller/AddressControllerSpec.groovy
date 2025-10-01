package cz.ivosahlik.ecommerce.controller

import cz.ivosahlik.ecommerce.model.User
import cz.ivosahlik.ecommerce.payload.AddressDTO
import cz.ivosahlik.ecommerce.security.jwt.JwtUtils
import cz.ivosahlik.ecommerce.security.services.UserDetailsServiceImpl
import cz.ivosahlik.ecommerce.service.AddressService
import cz.ivosahlik.ecommerce.util.AuthUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Specification

import static org.hamcrest.Matchers.*
import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.BDDMockito.given
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(controllers = AddressController)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration
class AddressControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @MockitoBean
    AuthUtil authUtil

    @MockitoBean
    AddressService addressService

    @MockitoBean
    JwtUtils jwtUtils

    @MockitoBean
    UserDetailsServiceImpl userDetailsService

    User user

    def setup() {
        user = new User("user1", "user1@example.com", "secret")
        user.setUserId(1L)
        given(authUtil.loggedInUser()).willReturn(user)
    }

    @WithMockUser(username = "user1", roles = ["USER"])
    def "create address returns 201 and body"() {
        given(addressService.createAddress(any(AddressDTO.class), eq(user))).willAnswer { inv ->
            AddressDTO dto = inv.arguments[0] as AddressDTO
            dto.setAddressId(10L)
            return dto
        }
        def payload = '''{
            "street":"Main St",
            "buildingName":"Bldg A",
            "city":"Prague",
            "state":"Praha",
            "country":"CZ",
            "pincode":"12345"
        }'''
        when:
        ResultActions result = mockMvc.perform(post("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
        then:
        result.andExpect(status().isCreated())
                .andExpect(jsonPath('$.addressId', is(10)))
                .andExpect(jsonPath('$.city', is('Prague')))
    }

    @WithMockUser(roles = ["USER"])
    def "get all addresses returns list"() {
        given(addressService.getAddresses()).willReturn([
                new AddressDTO(1L, 'S1', 'B1', 'C1', 'ST1', 'CT1', '11111'),
                new AddressDTO(2L, 'S2', 'B2', 'C2', 'ST2', 'CT2', '22222')
        ])
        when:
        def result = mockMvc.perform(get('/api/addresses'))
        then:
        result.andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(2)))
                .andExpect(jsonPath('$[1].pincode', is('22222')))
    }

    @WithMockUser(roles = ["USER"])
    def "get address by id returns address"() {
        given(addressService.getAddressesById(5L)).willReturn(new AddressDTO(5L, 'Str', 'Build', 'City', 'State', 'Country', '99999'))
        when:
        def result = mockMvc.perform(get('/api/addresses/5'))
        then:
        result.andExpect(status().isOk())
                .andExpect(jsonPath('$.addressId', is(5)))
                .andExpect(jsonPath('$.pincode', is('99999')))
    }

    @WithMockUser(roles = ["USER"])
    def "get user addresses returns list for logged in user"() {
        given(addressService.getUserAddresses(user)).willReturn([
                new AddressDTO(7L, 'AA', 'BB', 'CC', 'DD', 'EE', '10101')
        ])
        when:
        def result = mockMvc.perform(get('/api/users/addresses'))
        then:
        result.andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(1)))
                .andExpect(jsonPath('$[0].addressId', is(7)))
    }

    @WithMockUser(roles = ["USER"])
    def "update address returns updated dto"() {
        given(addressService.updateAddress(eq(3L), any(AddressDTO.class))).willReturn(
                new AddressDTO(3L, 'New', 'NB', 'NC', 'NS', 'CT', '30303')
        )
        def payload = '{"street":"X"}'
        when:
        def result = mockMvc.perform(put('/api/addresses/3')
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
        then:
        result.andExpect(status().isOk())
                .andExpect(jsonPath('$.addressId', is(3)))
                .andExpect(jsonPath('$.street', is('New')))
    }

    @WithMockUser(roles = ["USER"])
    def "delete address returns status string"() {
        given(addressService.deleteAddress(4L)).willReturn('Address deleted successfully with addressId: 4')
        when:
        def result = mockMvc.perform(delete('/api/addresses/4'))
        then:
        result.andExpect(status().isOk())
                .andExpect(content().string(containsString('4')))
    }
}

