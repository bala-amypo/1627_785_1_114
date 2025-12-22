// package com.example.demo.controller;

// import java.util.List;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RestController;

// import com.example.demo.entity.ColdRoom;
// import com.example.demo.service.ColdRoomService;

// @RestController
// public class ColdRoomController {

//     private final ColdRoomService coldRoomService;

//     public ColdRoomController(ColdRoomService coldRoomService) {
//         this.coldRoomService = coldRoomService;
//     }

//     // POST /cold-rooms
//     @PostMapping("/cold-rooms")
//     public ColdRoom createColdRoom(@RequestBody ColdRoom coldRoom) {
//         return coldRoomService.addColdRoom(coldRoom);
//     }

//     // GET /cold-rooms
//     @GetMapping("/cold-rooms")
//     public List<ColdRoom> getAllColdRooms() {
//         return coldRoomService.getAllColdRooms();
//     }
// }