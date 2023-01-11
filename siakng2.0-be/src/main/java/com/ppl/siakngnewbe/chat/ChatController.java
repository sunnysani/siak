package com.ppl.siakngnewbe.chat;


import com.ppl.siakngnewbe.rest.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatController {
    
    @Autowired
    private ChatService chatService;

    private static final String MESSAGES = "sukses";
    
    @PostMapping(path ="/post/mahasiswa", produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> postChatMahasiswa(@RequestHeader("Authorization") String token, @RequestBody Chat chat ) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage(MESSAGES);
        response.setResult(chatService.postChatMahasiswa(token, chat));
        return response;
    }

    @PostMapping(path = "/post/dosen/{npm}", produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> postChatDosen(@RequestHeader("Authorization") String token, @RequestBody Chat chat, @PathVariable(value = "npm") String npm) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage(MESSAGES);
        response.setResult(chatService.postChatDosen(token, chat, npm));
        return response;
    }

    @GetMapping(path = "/mahasiswa",  produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> getChatFromMahasiswa(@RequestHeader("Authorization") String token) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage(MESSAGES);
        response.setResult(chatService.getChatForMahasiswa(token));
        return response;
    }

    @GetMapping(path = "/dosen/{npm}",  produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> getChatFromDosen(@RequestHeader("Authorization") String token,@PathVariable(value = "npm") String npm) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage(MESSAGES);
        response.setResult(chatService.getChatForDosen(token, npm));
        return response;
    }

    @PostMapping(path = "/dosen/read/{npm}",  produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> readAllChatByDosen(@RequestHeader("Authorization") String token,@PathVariable(value = "npm") String npm) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage(MESSAGES);
        response.setResult(chatService.readChatMahasiswaByDosen(token, npm));
        return response;
    }

    @PostMapping(path = "/mahasiswa/read", produces = {"application/json"})
    @ResponseBody
    public BaseResponse<Object> readAllChatByMahasiswa(@RequestHeader("Authorization") String token) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage(MESSAGES);
        response.setResult(chatService.readChatDosenByMahasiswa(token));
        return response;
    }
}
