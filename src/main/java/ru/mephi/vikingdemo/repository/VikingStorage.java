package ru.mephi.vikingdemo.repository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.EquipmentItemEntity;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.model.VikingEntity;


@Repository
public class VikingStorage {

    private final VikingRepository vikingRepository;
    private final EquipmentItemRepository equipmentItemRepository;
    private final VikingMapper vikingMapper;

    public VikingStorage(
            VikingRepository vikingRepository,
            EquipmentItemRepository equipmentItemRepository,
            VikingMapper vikingMapper
    ) {
        this.vikingRepository = vikingRepository;
        this.equipmentItemRepository = equipmentItemRepository;
        this.vikingMapper = vikingMapper;
    }

    @Transactional
    public Viking save(Viking viking) {
        Integer vikingId = vikingRepository.save(
                vikingMapper.toVikingEntity(viking)
        );

        for (EquipmentItem item : equipmentOf(viking)) {
            equipmentItemRepository.save(
                    vikingMapper.toEquipmentItemEntity(vikingId, item)
            );
        }

        return vikingMapper.withId(vikingId, viking);
    }

    public List<Viking> findAll() {
        List<VikingEntity> vikingEntities = vikingRepository.findAll();
        List<EquipmentItemEntity> equipmentEntities = equipmentItemRepository.findAll();

        Map<Integer, List<EquipmentItemEntity>> equipmentByVikingId = equipmentEntities.stream()
                .collect(Collectors.groupingBy(EquipmentItemEntity::vikingId));

        return vikingEntities.stream()
                .map(vikingEntity -> vikingMapper.toViking(
                        vikingEntity,
                        equipmentByVikingId.getOrDefault(vikingEntity.id(), List.of())
                ))
                .toList();
    }

    @Transactional
    public Viking updateById(int id, Viking viking) {
        VikingEntity entity = vikingMapper.toVikingEntity(vikingMapper.withId(id, viking));
        boolean updated = vikingRepository.updateById(id, entity);

        if (!updated) {
            throw new NoSuchElementException("Викинг с id " + id + " не найден");
        }

        equipmentItemRepository.deleteByVikingId(id);

        for (EquipmentItem item : equipmentOf(viking)) {
            equipmentItemRepository.save(
                    vikingMapper.toEquipmentItemEntity(id, item)
            );
        }

        VikingEntity updatedEntity = vikingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Викинг с id " + id + " не найден"));
        List<EquipmentItemEntity> equipment = equipmentItemRepository.findByVikingId(id);

        return vikingMapper.toViking(updatedEntity, equipment);
    }

    @Transactional
    public void deleteById(int id) {
        vikingRepository.deleteById(id);
    }

    private List<EquipmentItem> equipmentOf(Viking viking) {
        if (viking.equipment() == null) {
            return List.of();
        }

        return viking.equipment();
    }
}
