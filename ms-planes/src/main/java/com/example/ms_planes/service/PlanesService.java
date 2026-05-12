package com.example.ms_planes.service;

import com.example.ms_planes.repository.PagosRepository;
import com.example.ms_planes.repository.PlanesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanesService {
    private final PlanesRepository planesRepository;
    private final PagosRepository pagosRepository;
}
