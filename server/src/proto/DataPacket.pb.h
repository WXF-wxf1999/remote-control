// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: DataPacket.proto

#ifndef GOOGLE_PROTOBUF_INCLUDED_DataPacket_2eproto
#define GOOGLE_PROTOBUF_INCLUDED_DataPacket_2eproto

#include <limits>
#include <string>

#include <google/protobuf/port_def.inc>
#if PROTOBUF_VERSION < 3019000
#error This file was generated by a newer version of protoc which is
#error incompatible with your Protocol Buffer headers. Please update
#error your headers.
#endif
#if 3019004 < PROTOBUF_MIN_PROTOC_VERSION
#error This file was generated by an older version of protoc which is
#error incompatible with your Protocol Buffer headers. Please
#error regenerate this file with a newer version of protoc.
#endif

#include <google/protobuf/port_undef.inc>
#include <google/protobuf/io/coded_stream.h>
#include <google/protobuf/arena.h>
#include <google/protobuf/arenastring.h>
#include <google/protobuf/generated_message_table_driven.h>
#include <google/protobuf/generated_message_util.h>
#include <google/protobuf/metadata_lite.h>
#include <google/protobuf/generated_message_reflection.h>
#include <google/protobuf/message.h>
#include <google/protobuf/repeated_field.h>  // IWYU pragma: export
#include <google/protobuf/extension_set.h>  // IWYU pragma: export
#include <google/protobuf/unknown_field_set.h>
// @@protoc_insertion_point(includes)
#include <google/protobuf/port_def.inc>
#define PROTOBUF_INTERNAL_EXPORT_DataPacket_2eproto
PROTOBUF_NAMESPACE_OPEN
namespace internal {
class AnyMetadata;
}  // namespace internal
PROTOBUF_NAMESPACE_CLOSE

// Internal implementation detail -- do not use these members.
struct TableStruct_DataPacket_2eproto {
  static const ::PROTOBUF_NAMESPACE_ID::internal::ParseTableField entries[]
    PROTOBUF_SECTION_VARIABLE(protodesc_cold);
  static const ::PROTOBUF_NAMESPACE_ID::internal::AuxiliaryParseTableField aux[]
    PROTOBUF_SECTION_VARIABLE(protodesc_cold);
  static const ::PROTOBUF_NAMESPACE_ID::internal::ParseTable schema[1]
    PROTOBUF_SECTION_VARIABLE(protodesc_cold);
  static const ::PROTOBUF_NAMESPACE_ID::internal::FieldMetadata field_metadata[];
  static const ::PROTOBUF_NAMESPACE_ID::internal::SerializationTable serialization_table[];
  static const uint32_t offsets[];
};
extern const ::PROTOBUF_NAMESPACE_ID::internal::DescriptorTable descriptor_table_DataPacket_2eproto;
class Packet;
struct PacketDefaultTypeInternal;
extern PacketDefaultTypeInternal _Packet_default_instance_;
PROTOBUF_NAMESPACE_OPEN
template<> ::Packet* Arena::CreateMaybeMessage<::Packet>(Arena*);
PROTOBUF_NAMESPACE_CLOSE

// ===================================================================

class Packet final :
    public ::PROTOBUF_NAMESPACE_ID::Message /* @@protoc_insertion_point(class_definition:Packet) */ {
 public:
  inline Packet() : Packet(nullptr) {}
  ~Packet() override;
  explicit constexpr Packet(::PROTOBUF_NAMESPACE_ID::internal::ConstantInitialized);

  Packet(const Packet& from);
  Packet(Packet&& from) noexcept
    : Packet() {
    *this = ::std::move(from);
  }

  inline Packet& operator=(const Packet& from) {
    CopyFrom(from);
    return *this;
  }
  inline Packet& operator=(Packet&& from) noexcept {
    if (this == &from) return *this;
    if (GetOwningArena() == from.GetOwningArena()
  #ifdef PROTOBUF_FORCE_COPY_IN_MOVE
        && GetOwningArena() != nullptr
  #endif  // !PROTOBUF_FORCE_COPY_IN_MOVE
    ) {
      InternalSwap(&from);
    } else {
      CopyFrom(from);
    }
    return *this;
  }

  static const ::PROTOBUF_NAMESPACE_ID::Descriptor* descriptor() {
    return GetDescriptor();
  }
  static const ::PROTOBUF_NAMESPACE_ID::Descriptor* GetDescriptor() {
    return default_instance().GetMetadata().descriptor;
  }
  static const ::PROTOBUF_NAMESPACE_ID::Reflection* GetReflection() {
    return default_instance().GetMetadata().reflection;
  }
  static const Packet& default_instance() {
    return *internal_default_instance();
  }
  static inline const Packet* internal_default_instance() {
    return reinterpret_cast<const Packet*>(
               &_Packet_default_instance_);
  }
  static constexpr int kIndexInFileMessages =
    0;

  friend void swap(Packet& a, Packet& b) {
    a.Swap(&b);
  }
  inline void Swap(Packet* other) {
    if (other == this) return;
  #ifdef PROTOBUF_FORCE_COPY_IN_SWAP
    if (GetOwningArena() != nullptr &&
        GetOwningArena() == other->GetOwningArena()) {
   #else  // PROTOBUF_FORCE_COPY_IN_SWAP
    if (GetOwningArena() == other->GetOwningArena()) {
  #endif  // !PROTOBUF_FORCE_COPY_IN_SWAP
      InternalSwap(other);
    } else {
      ::PROTOBUF_NAMESPACE_ID::internal::GenericSwap(this, other);
    }
  }
  void UnsafeArenaSwap(Packet* other) {
    if (other == this) return;
    GOOGLE_DCHECK(GetOwningArena() == other->GetOwningArena());
    InternalSwap(other);
  }

  // implements Message ----------------------------------------------

  Packet* New(::PROTOBUF_NAMESPACE_ID::Arena* arena = nullptr) const final {
    return CreateMaybeMessage<Packet>(arena);
  }
  using ::PROTOBUF_NAMESPACE_ID::Message::CopyFrom;
  void CopyFrom(const Packet& from);
  using ::PROTOBUF_NAMESPACE_ID::Message::MergeFrom;
  void MergeFrom(const Packet& from);
  private:
  static void MergeImpl(::PROTOBUF_NAMESPACE_ID::Message* to, const ::PROTOBUF_NAMESPACE_ID::Message& from);
  public:
  PROTOBUF_ATTRIBUTE_REINITIALIZES void Clear() final;
  bool IsInitialized() const final;

  size_t ByteSizeLong() const final;
  const char* _InternalParse(const char* ptr, ::PROTOBUF_NAMESPACE_ID::internal::ParseContext* ctx) final;
  uint8_t* _InternalSerialize(
      uint8_t* target, ::PROTOBUF_NAMESPACE_ID::io::EpsCopyOutputStream* stream) const final;
  int GetCachedSize() const final { return _cached_size_.Get(); }

  private:
  void SharedCtor();
  void SharedDtor();
  void SetCachedSize(int size) const final;
  void InternalSwap(Packet* other);

  private:
  friend class ::PROTOBUF_NAMESPACE_ID::internal::AnyMetadata;
  static ::PROTOBUF_NAMESPACE_ID::StringPiece FullMessageName() {
    return "Packet";
  }
  protected:
  explicit Packet(::PROTOBUF_NAMESPACE_ID::Arena* arena,
                       bool is_message_owned = false);
  private:
  static void ArenaDtor(void* object);
  inline void RegisterArenaDtor(::PROTOBUF_NAMESPACE_ID::Arena* arena);
  public:

  static const ClassData _class_data_;
  const ::PROTOBUF_NAMESPACE_ID::Message::ClassData*GetClassData() const final;

  ::PROTOBUF_NAMESPACE_ID::Metadata GetMetadata() const final;

  // nested types ----------------------------------------------------

  // accessors -------------------------------------------------------

  enum : int {
    kDataSegment1FieldNumber = 3,
    kDataSegment2FieldNumber = 4,
    kSessionIdFieldNumber = 1,
    kMessageTypeFieldNumber = 2,
  };
  // bytes dataSegment1 = 3;
  void clear_datasegment1();
  const std::string& datasegment1() const;
  template <typename ArgT0 = const std::string&, typename... ArgT>
  void set_datasegment1(ArgT0&& arg0, ArgT... args);
  std::string* mutable_datasegment1();
  PROTOBUF_NODISCARD std::string* release_datasegment1();
  void set_allocated_datasegment1(std::string* datasegment1);
  private:
  const std::string& _internal_datasegment1() const;
  inline PROTOBUF_ALWAYS_INLINE void _internal_set_datasegment1(const std::string& value);
  std::string* _internal_mutable_datasegment1();
  public:

  // bytes dataSegment2 = 4;
  void clear_datasegment2();
  const std::string& datasegment2() const;
  template <typename ArgT0 = const std::string&, typename... ArgT>
  void set_datasegment2(ArgT0&& arg0, ArgT... args);
  std::string* mutable_datasegment2();
  PROTOBUF_NODISCARD std::string* release_datasegment2();
  void set_allocated_datasegment2(std::string* datasegment2);
  private:
  const std::string& _internal_datasegment2() const;
  inline PROTOBUF_ALWAYS_INLINE void _internal_set_datasegment2(const std::string& value);
  std::string* _internal_mutable_datasegment2();
  public:

  // uint32 sessionId = 1;
  void clear_sessionid();
  uint32_t sessionid() const;
  void set_sessionid(uint32_t value);
  private:
  uint32_t _internal_sessionid() const;
  void _internal_set_sessionid(uint32_t value);
  public:

  // uint32 messageType = 2;
  void clear_messagetype();
  uint32_t messagetype() const;
  void set_messagetype(uint32_t value);
  private:
  uint32_t _internal_messagetype() const;
  void _internal_set_messagetype(uint32_t value);
  public:

  // @@protoc_insertion_point(class_scope:Packet)
 private:
  class _Internal;

  template <typename T> friend class ::PROTOBUF_NAMESPACE_ID::Arena::InternalHelper;
  typedef void InternalArenaConstructable_;
  typedef void DestructorSkippable_;
  ::PROTOBUF_NAMESPACE_ID::internal::ArenaStringPtr datasegment1_;
  ::PROTOBUF_NAMESPACE_ID::internal::ArenaStringPtr datasegment2_;
  uint32_t sessionid_;
  uint32_t messagetype_;
  mutable ::PROTOBUF_NAMESPACE_ID::internal::CachedSize _cached_size_;
  friend struct ::TableStruct_DataPacket_2eproto;
};
// ===================================================================


// ===================================================================

#ifdef __GNUC__
  #pragma GCC diagnostic push
  #pragma GCC diagnostic ignored "-Wstrict-aliasing"
#endif  // __GNUC__
// Packet

// uint32 sessionId = 1;
inline void Packet::clear_sessionid() {
  sessionid_ = 0u;
}
inline uint32_t Packet::_internal_sessionid() const {
  return sessionid_;
}
inline uint32_t Packet::sessionid() const {
  // @@protoc_insertion_point(field_get:Packet.sessionId)
  return _internal_sessionid();
}
inline void Packet::_internal_set_sessionid(uint32_t value) {
  
  sessionid_ = value;
}
inline void Packet::set_sessionid(uint32_t value) {
  _internal_set_sessionid(value);
  // @@protoc_insertion_point(field_set:Packet.sessionId)
}

// uint32 messageType = 2;
inline void Packet::clear_messagetype() {
  messagetype_ = 0u;
}
inline uint32_t Packet::_internal_messagetype() const {
  return messagetype_;
}
inline uint32_t Packet::messagetype() const {
  // @@protoc_insertion_point(field_get:Packet.messageType)
  return _internal_messagetype();
}
inline void Packet::_internal_set_messagetype(uint32_t value) {
  
  messagetype_ = value;
}
inline void Packet::set_messagetype(uint32_t value) {
  _internal_set_messagetype(value);
  // @@protoc_insertion_point(field_set:Packet.messageType)
}

// bytes dataSegment1 = 3;
inline void Packet::clear_datasegment1() {
  datasegment1_.ClearToEmpty();
}
inline const std::string& Packet::datasegment1() const {
  // @@protoc_insertion_point(field_get:Packet.dataSegment1)
  return _internal_datasegment1();
}
template <typename ArgT0, typename... ArgT>
inline PROTOBUF_ALWAYS_INLINE
void Packet::set_datasegment1(ArgT0&& arg0, ArgT... args) {
 
 datasegment1_.SetBytes(::PROTOBUF_NAMESPACE_ID::internal::ArenaStringPtr::EmptyDefault{}, static_cast<ArgT0 &&>(arg0), args..., GetArenaForAllocation());
  // @@protoc_insertion_point(field_set:Packet.dataSegment1)
}
inline std::string* Packet::mutable_datasegment1() {
  std::string* _s = _internal_mutable_datasegment1();
  // @@protoc_insertion_point(field_mutable:Packet.dataSegment1)
  return _s;
}
inline const std::string& Packet::_internal_datasegment1() const {
  return datasegment1_.Get();
}
inline void Packet::_internal_set_datasegment1(const std::string& value) {
  
  datasegment1_.Set(::PROTOBUF_NAMESPACE_ID::internal::ArenaStringPtr::EmptyDefault{}, value, GetArenaForAllocation());
}
inline std::string* Packet::_internal_mutable_datasegment1() {
  
  return datasegment1_.Mutable(::PROTOBUF_NAMESPACE_ID::internal::ArenaStringPtr::EmptyDefault{}, GetArenaForAllocation());
}
inline std::string* Packet::release_datasegment1() {
  // @@protoc_insertion_point(field_release:Packet.dataSegment1)
  return datasegment1_.Release(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArenaForAllocation());
}
inline void Packet::set_allocated_datasegment1(std::string* datasegment1) {
  if (datasegment1 != nullptr) {
    
  } else {
    
  }
  datasegment1_.SetAllocated(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), datasegment1,
      GetArenaForAllocation());
#ifdef PROTOBUF_FORCE_COPY_DEFAULT_STRING
  if (datasegment1_.IsDefault(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited())) {
    datasegment1_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), "", GetArenaForAllocation());
  }
#endif // PROTOBUF_FORCE_COPY_DEFAULT_STRING
  // @@protoc_insertion_point(field_set_allocated:Packet.dataSegment1)
}

// bytes dataSegment2 = 4;
inline void Packet::clear_datasegment2() {
  datasegment2_.ClearToEmpty();
}
inline const std::string& Packet::datasegment2() const {
  // @@protoc_insertion_point(field_get:Packet.dataSegment2)
  return _internal_datasegment2();
}
template <typename ArgT0, typename... ArgT>
inline PROTOBUF_ALWAYS_INLINE
void Packet::set_datasegment2(ArgT0&& arg0, ArgT... args) {
 
 datasegment2_.SetBytes(::PROTOBUF_NAMESPACE_ID::internal::ArenaStringPtr::EmptyDefault{}, static_cast<ArgT0 &&>(arg0), args..., GetArenaForAllocation());
  // @@protoc_insertion_point(field_set:Packet.dataSegment2)
}
inline std::string* Packet::mutable_datasegment2() {
  std::string* _s = _internal_mutable_datasegment2();
  // @@protoc_insertion_point(field_mutable:Packet.dataSegment2)
  return _s;
}
inline const std::string& Packet::_internal_datasegment2() const {
  return datasegment2_.Get();
}
inline void Packet::_internal_set_datasegment2(const std::string& value) {
  
  datasegment2_.Set(::PROTOBUF_NAMESPACE_ID::internal::ArenaStringPtr::EmptyDefault{}, value, GetArenaForAllocation());
}
inline std::string* Packet::_internal_mutable_datasegment2() {
  
  return datasegment2_.Mutable(::PROTOBUF_NAMESPACE_ID::internal::ArenaStringPtr::EmptyDefault{}, GetArenaForAllocation());
}
inline std::string* Packet::release_datasegment2() {
  // @@protoc_insertion_point(field_release:Packet.dataSegment2)
  return datasegment2_.Release(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), GetArenaForAllocation());
}
inline void Packet::set_allocated_datasegment2(std::string* datasegment2) {
  if (datasegment2 != nullptr) {
    
  } else {
    
  }
  datasegment2_.SetAllocated(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), datasegment2,
      GetArenaForAllocation());
#ifdef PROTOBUF_FORCE_COPY_DEFAULT_STRING
  if (datasegment2_.IsDefault(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited())) {
    datasegment2_.Set(&::PROTOBUF_NAMESPACE_ID::internal::GetEmptyStringAlreadyInited(), "", GetArenaForAllocation());
  }
#endif // PROTOBUF_FORCE_COPY_DEFAULT_STRING
  // @@protoc_insertion_point(field_set_allocated:Packet.dataSegment2)
}

#ifdef __GNUC__
  #pragma GCC diagnostic pop
#endif  // __GNUC__

// @@protoc_insertion_point(namespace_scope)


// @@protoc_insertion_point(global_scope)

#include <google/protobuf/port_undef.inc>
#endif  // GOOGLE_PROTOBUF_INCLUDED_GOOGLE_PROTOBUF_INCLUDED_DataPacket_2eproto
