package poc.representation.systemManage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import poc.application.systemManage.CompilerApplicationService;
import poc.application.systemManage.dto.CompilerDto;
import poc.domain.systemManage.model.Page;
import poc.representation.aop.annotation.SystemControllerLog;
import poc.representation.response.Response;

/**
 * @author ManJiis
 * @version V1.0
 * @since 2020/9/3 17:23
 */
@Api(value = "编制人员")
@RequestMapping("/compiler")
public class CompilerController {

    public final CompilerApplicationService compilerService;

    public CompilerController(CompilerApplicationService compilerService){
        this.compilerService = compilerService;
    }

    @SystemControllerLog(description = "新增编制人员信息")
    @PostMapping("/insertCompiler")
    @ApiOperation(value = "新增编制人员信息")
    public Response insertCompiler(CompilerDto compiler) {
        compilerService.insert(compiler);
        return Response.ok("添加成功。");
    }
    @SystemControllerLog(description = "修改编制人员信息")
    @PostMapping("/updateCompilerById")
    @ApiOperation(value = "修改编制人员信息")
    public Response updateCompilerById(CompilerDto compiler) {
        compilerService.updateById(compiler);
        return Response.ok("修改成功。");
    }

    @SystemControllerLog(description = "删除编制人员信息")
    @PostMapping("/deleteCompilerById")
    @ApiOperation(value = "删除编制人员信息")
    public Response deleteCompilerById(String id) {
        compilerService.deleteById(id);
        return Response.ok("删除成功。");
    }
    @SystemControllerLog(description = "查看编制人员列表")
    @GetMapping("/getCompilerList")
    @ApiOperation(value = "获取编制人员列表")
    public Response getCompilerList(String name, String orgName, int pageNum, int pageSize) {
        return Response.ok(compilerService.getList(name, orgName, new Page(pageNum,pageSize)));
    }
}
