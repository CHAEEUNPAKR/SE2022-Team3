package closet.backend.service;

import closet.backend.dto.cloth.UpdateClothDto;
import closet.backend.req.cloth.*;
import closet.backend.util.AuthUtil;
import closet.backend.util.FileUtil;
import closet.backend.dao.cloth.ClothDao;
import closet.backend.dto.cloth.ChangeFavoriteDto;
import closet.backend.dto.cloth.ClothDto;
import closet.backend.dto.cloth.CreateClothDto;
import closet.backend.exception.ClothException;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClothService {

    @Autowired
    private final ClothDao clothDao;
    @Autowired
    private final AuthUtil authUtil;
    private final FileUtil fileUtil;

    public ClothDto createCloth(CreateClothReq createClothReq, Optional<MultipartFile> img) throws FirebaseAuthException, IOException {
        int id = authUtil.getUserId(createClothReq.getIdToken());
        String file_name;
        if(img.isPresent()){
            file_name = fileUtil.uploadFile(img.get());
        }else{
            file_name = "";
        }
        CreateClothDto createClothDto = new CreateClothDto(id,createClothReq.getName(),createClothReq.getSeason(),createClothReq.getCategory(),createClothReq.getBrand()
                ,createClothReq.getPlace(),createClothReq.getSize(),createClothReq.getCloth_body(),file_name);
        ClothDto clothDto = clothDao.save(createClothDto);
        return clothDto;
    }

    public ClothDto updateCloth(UpdateClothReq updateClothReq, Optional<MultipartFile> img) throws FirebaseAuthException,IOException{
        int id = authUtil.getUserId(updateClothReq.getIdToken());
        String file_name;
        if(id == clothDao.findWriterId(updateClothReq.getCloth_id())){
            if(img.isPresent()){
                file_name = fileUtil.uploadFile(img.get());
            }else{
                file_name = "";
            }
            UpdateClothDto updateClothDto = new UpdateClothDto(id, updateClothReq.getCloth_id(), updateClothReq.getName(), updateClothReq.getSeason(),
                    updateClothReq.getCategory(), updateClothReq.getBrand(), updateClothReq.getPlace(), updateClothReq.getSize(), updateClothReq.getCloth_body(), file_name);
            ClothDto result = clothDao.update(updateClothDto);
            return result;
        } else{
            throw new ClothException("의류 등록자가 아닙니다.",403);
        }
    }

    public String deleteCloth(DeleteClothReq deleteClothReq) throws FirebaseAuthException{
        int id = authUtil.getUserId(deleteClothReq.getIdToken());
        if( id == clothDao.findWriterId(deleteClothReq.getCloth_id())) {
            String result = clothDao.delete(deleteClothReq.getCloth_id());
            return result;
        } else{
            throw new ClothException("의류 등록자가 아닙니다.",403);
        }
    }

    public List<ClothDto> getCloth(String idToken) throws FirebaseAuthException {
        int id = authUtil.getUserId(idToken);
        List<ClothDto> result = clothDao.findById(id);
        return result;
    }

    public ClothDto changeFavorite(ChangeFavoriteReq changeFavoriteReq) throws FirebaseAuthException{
        int id = authUtil.getUserId(changeFavoriteReq.getIdToken());
        ChangeFavoriteDto changeFavoriteDto = new ChangeFavoriteDto(id, changeFavoriteReq.getCloth_id(), changeFavoriteReq.getFavorite());
        ClothDto result;
        if(changeFavoriteDto.getFavorite() == 1){
            result = clothDao.removeFavorite(changeFavoriteDto.getCloth_id());
        } else{
            result = clothDao.setFavorite(changeFavoriteDto.getCloth_id());
        }
        return result;
    }

    public ClothDto getClothDetail(int cloth_id){
        ClothDto result = clothDao.findByClothId(cloth_id);
        return result;
    }

    public List<ClothDto> getClothBySeason(GetClothBySeasonReq getClothBySeasonReq) throws FirebaseAuthException{
        int id = authUtil.getUserId(getClothBySeasonReq.getIdToken());
        List<ClothDto> result = clothDao.findBySeason(id, getClothBySeasonReq.getSeason());
        return result;
    }

    public List<ClothDto> getClothByCategory(GetClothByCategoryReq getClothByCategoryReq) throws FirebaseAuthException{
        int id = authUtil.getUserId(getClothByCategoryReq.getIdToken());
        List<ClothDto> result = clothDao.findByCategory(id, getClothByCategoryReq.getCategory());
        return result;
    }

    public List<ClothDto> getFavoriteCloth(String idToken) throws FirebaseAuthException{
        int id = authUtil.getUserId(idToken);
        List<ClothDto> result = clothDao.findByFavorite(id);
        return result;
    }

}
