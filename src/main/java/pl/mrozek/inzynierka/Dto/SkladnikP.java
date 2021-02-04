package pl.mrozek.inzynierka.Dto;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SkladnikP {
    private double iloscML;
    private String nazwa;
    private int rodzaj;
    private String typ;
    private String opisDodatkowy;
    private boolean nowy;
    private boolean nowyAlko;


}
//todo nowa nazwa nie kasuje aktualnie zaznaczonego typu
// displayflex do zdjecia (css triks) flexbox grid
// edycja
// wybor miejsca na zdjecie i obrobka pola
// wybor miejsca dodawania zdjecia- czy da się z mau głównego
// dodawanie zdjec
// wyswietlanie zdjec


// wklejanie zdjecia


//    @GetMapping("/image/display/{id}")
//    @ResponseBody
//    void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<ImageGallery> imageGallery)
//            throws ServletException, IOException {
//        log.info("Id :: " + id);
//        imageGallery = imageGalleryService.getImageById(id);
//        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
//        response.getOutputStream().write(imageGallery.get().getImage());
//        response.getOutputStream().close();
//    }


//<tbody th:with="count=0">
//<tr th:each = "imageGallery, hh : ${images}">
//<td th:with="count=${count + 1}" th:text="${count}"></td>
//<td th:text="${imageGallery.name}"></td>
//<td><img th:src="@{${'/image/display/'+ imageGallery.id}}"
//class="card img-fluid" style="width:300px" alt=""/></td>
//<td th:text="${imageGallery.description}"></td>
//<td th:text="${imageGallery.price}"></td>
//<td th:text="${#dates.format({imageGallery.createDate}, 'dd-MM-yyyy')}"/></td>
//<td><a th:href="@{/image/imageDetails(id=${imageGallery.id})}" class="btn btn-info text-right" target="_blank">View</a></td>
//</tr>
//</tbody>