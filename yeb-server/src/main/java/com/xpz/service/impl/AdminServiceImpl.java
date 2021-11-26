package com.xpz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xpz.utils.AdminUtils;
import com.xpz.config.security.JwtTokenUtil;
import com.xpz.mapper.AdminRoleMapper;
import com.xpz.mapper.RoleMapper;
import com.xpz.pojo.Admin;
import com.xpz.mapper.AdminMapper;
import com.xpz.pojo.AdminRole;
import com.xpz.pojo.RespBean;
import com.xpz.pojo.Role;
import com.xpz.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Catherine
 * @since 2021-09-28
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;

    /**
     * 登录之后返回 token
     */
    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
        //验证码验证
        String kaptcha = (String) request.getSession().getAttribute("kaptcha");
        if(null == code||!code.equalsIgnoreCase(kaptcha)){
            return RespBean.error("验证码错误，请重新输入");
        }
        //登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(null == userDetails||passwordEncoder.matches(password, userDetails.getPassword())){
            return RespBean.error("用户名或密码不正确");
        }
        if(!userDetails.isEnabled()){
            return RespBean.error("账号被禁，请联系管理员！");
        }
        //更新security对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        //生成 token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMapper = new HashMap<>();
        tokenMapper.put("token", token);
        tokenMapper.put("tokenHead", tokenHead);
        return RespBean.sucess("登录成功", tokenMapper);
    }

    @Override
    public Admin getAdminByUserName(String username){
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username).eq("enabled", true));
        return admin;
    }

    /**
     * 根据用户查询角色
     */
    @Override
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);
    }

    /**
     * 获取所有操作员
     */
    @Override
    public List<Admin> getAllAdmins(String keywords) {
        return adminMapper.getAllAdmins(AdminUtils.getCurrentAdmin().getId(), keywords);
    }

    /**
     * 更新操作员角色
     */
    @Override
    @Transactional
    public RespBean updateAdminRole(Integer id, int[] rids) {
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId",id));
        Integer result = adminRoleMapper.addAdminRole(id, rids);
        if (result == rids.length){
            return RespBean.sucess("更新操作员角色成功！");
        }
        return RespBean.sucess("更新操作员角色失败！");
    }

    /**
     * 更新当前用户密码
     */
    @Override
    public RespBean updatePwd(String oldPwd, String newPwd, int id) {
        Admin admin = adminMapper.selectById(id);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPwd, admin.getPassword())){
            admin.setPassword(newPwd);
            adminMapper.updateById(admin);
            return RespBean.sucess("更新成功！");
        }
        return RespBean.error("更新失败！");
    }
}
