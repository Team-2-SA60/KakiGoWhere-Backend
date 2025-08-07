package team2.kakigowherebackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public class TouristUpdateRequestDTO {
    @NotBlank
    private String name;

    @Size(max = 3)
    private List<Long> interestCategoryIds;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Long> getInterestCategoryIds() {
        return interestCategoryIds;
    }
    public void setInterestCategoryIds(List<Long> interestCategoryIds) {
        this.interestCategoryIds = interestCategoryIds;
    }
}