package com.example.gbdpuserac.mapper;


import com.example.gbdpbootcore.core.IMapper;
import com.example.gbdpuserac.model.UacUserToken;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;
import java.util.Map;
@Mapper
public interface UacUserTokenMapper extends IMapper<UacUserToken> {
    /**
     * 超时token更新为离线.
     *
     * @param map the map
     * @return the int
     */
    int batchUpdateTokenOffLine(Map<String, Object> map);

    /**
     * 查询超时token Id 集合.
     *
     * @return the listByPage
     */
    List<Long> listOffLineTokenId();
}